package com.omvaultchain.storage.controller;

import com.omvaultchain.storage.model.SearchRequest;
import com.omvaultchain.storage.model.SearchResponse;
import com.omvaultchain.storage.service.FileSearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/storage/search")
@RequiredArgsConstructor
public class SearchController {
    private final FileSearchService fileSearchService;
    /**
     * Searches for files owned by the authenticated user using filters like
     * name, MIME type, size, and creation date.
     *
     * Uses pagination and sorting.
     *
     * 🔐 Only returns files belonging to the user identified by the request header.
     *
     * Request Body Example:
     * {
     *   "nameContains": "invoice",
     *   "mimeType": "application/pdf",
     *   "minSize": 1000,
     *   "maxSize": 5000000,
     *   "startDate": "2024-01-01T00:00:00Z",
     *   "endDate": "2024-12-31T23:59:59Z",
     *   "sortBy": "created_at",
     *   "sortOrder": "desc",
     *   "page": 0,
     *   "size": 10
     * }
     *
     * Pagination Info:
     * - page: Zero-based index (0 = first page)
     * - size: Number of results per page
     *
     * Sorting:
     * - sortBy: Field to sort by (e.g., "created_at", "name", "size_bytes")
     * - sortOrder: "asc" or "desc"
     *
     * @param request SearchRequest containing all optional filters and pagination
     * @param userId  User ID extracted from header or JWT
     * @return A paginated list of file metadata matching the search criteria
     */
    @PostMapping("/metadata")
    public ResponseEntity<?> searchFilesByMetaData(@RequestBody SearchRequest request, @RequestHeader("X-User-ID") String userId) {

        SearchResponse response = fileSearchService.searchFilesByMetaData(request, userId);
        return ResponseEntity.ok(response);
    }
}
