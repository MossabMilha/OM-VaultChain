package com.omvaultchain.storage.service;

import com.omvaultchain.storage.model.FileMetadata;
import com.omvaultchain.storage.model.SearchRequest;
import com.omvaultchain.storage.model.SearchResponse;
import com.omvaultchain.storage.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileSearchService {
    private final FileMetadataRepository fileMetadataRepository;
    public SearchResponse searchFilesByMetaData(SearchRequest request,String userId){
        Pageable pageable = PageRequest.of(
                request.getPage(), request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSortOrder()), request.getSortBy())
        );

        Specification<FileMetadata> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("ownerId"), userId));
            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (request.getNameContains() != null) {
                predicates.add(cb.like(cb.lower(root.get("fileName")), "%" + request.getNameContains().toLowerCase() + "%"));
            }

            if (request.getMimeType() != null) {
                predicates.add(cb.equal(root.get("mimeType"), request.getMimeType()));
            }

            if (request.getMinSize() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("sizeBytes"), request.getMinSize()));
            }

            if (request.getMaxSize() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("sizeBytes"), request.getMaxSize()));
            }

            if (request.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getStartDate()));
            }

            if (request.getEndDate() != null) {
                Instant end = Instant.parse(request.getEndDate());
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), end));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<FileMetadata> pageResult = fileMetadataRepository.findAll(spec, pageable);

        SearchResponse response = new SearchResponse();
        response.setResults(pageResult.getContent());
        response.setTotalElements(pageResult.getTotalElements());
        response.setTotalPages(pageResult.getTotalPages());
        response.setCurrentPage(pageResult.getNumber());

        return response;
    }

}
