package com.omvaultchain.blockchain.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/LFDT-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.7.0.
 */
@SuppressWarnings("rawtypes")
public class VersionManager extends Contract {
    public static final String BINARY = "0x6080604052348015600f57600080fd5b50610cf18061001f6000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063460e3ca21461004657806356f0a106146100785780639a00e589146100a8575b600080fd5b610060600480360381019061005b919061055e565b6100c4565b60405161006f93929190610648565b60405180910390f35b610092600480360381019061008d9190610686565b6101a9565b60405161009f919061083a565b60405180910390f35b6100c260048036038101906100bd919061085c565b6102cc565b005b60008280516020810182018051848252602083016020850120818352809550505050505081815481106100f657600080fd5b90600052602060002090600302016000915091505080600001805461011a90610903565b80601f016020809104026020016040519081016040528092919081815260200182805461014690610903565b80156101935780601f1061016857610100808354040283529160200191610193565b820191906000526020600020905b81548152906001019060200180831161017657829003601f168201915b5050505050908060010154908060020154905083565b60606000826040516101bb9190610970565b9081526020016040518091039020805480602002602001604051908101604052809291908181526020016000905b828210156102c1578382906000526020600020906003020160405180606001604052908160008201805461021c90610903565b80601f016020809104026020016040519081016040528092919081815260200182805461024890610903565b80156102955780601f1061026a57610100808354040283529160200191610295565b820191906000526020600020905b81548152906001019060200180831161027857829003601f168201915b5050505050815260200160018201548152602001600282015481525050815260200190600101906101e9565b505050509050919050565b600060016000846040516102e09190610970565b9081526020016040518091039020805490506102fc91906109b6565b905060008360405161030e9190610970565b9081526020016040518091039020604051806060016040528084815260200183815260200142815250908060018154018082558091505060019003906000526020600020906003020160009091909190915060008201518160000190816103759190610b96565b50602082015181600101556040820151816002015550507f809733ae1220f7032288ee0ad1846c855a21c243208052b847b01b90dc7ad05b838383426040516103c19493929190610c68565b60405180910390a1505050565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b610435826103ec565b810181811067ffffffffffffffff82111715610454576104536103fd565b5b80604052505050565b60006104676103ce565b9050610473828261042c565b919050565b600067ffffffffffffffff821115610493576104926103fd565b5b61049c826103ec565b9050602081019050919050565b82818337600083830152505050565b60006104cb6104c684610478565b61045d565b9050828152602081018484840111156104e7576104e66103e7565b5b6104f28482856104a9565b509392505050565b600082601f83011261050f5761050e6103e2565b5b813561051f8482602086016104b8565b91505092915050565b6000819050919050565b61053b81610528565b811461054657600080fd5b50565b60008135905061055881610532565b92915050565b60008060408385031215610575576105746103d8565b5b600083013567ffffffffffffffff811115610593576105926103dd565b5b61059f858286016104fa565b92505060206105b085828601610549565b9150509250929050565b600081519050919050565b600082825260208201905092915050565b60005b838110156105f45780820151818401526020810190506105d9565b60008484015250505050565b600061060b826105ba565b61061581856105c5565b93506106258185602086016105d6565b61062e816103ec565b840191505092915050565b61064281610528565b82525050565b600060608201905081810360008301526106628186610600565b90506106716020830185610639565b61067e6040830184610639565b949350505050565b60006020828403121561069c5761069b6103d8565b5b600082013567ffffffffffffffff8111156106ba576106b96103dd565b5b6106c6848285016104fa565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b600082825260208201905092915050565b6000610717826105ba565b61072181856106fb565b93506107318185602086016105d6565b61073a816103ec565b840191505092915050565b61074e81610528565b82525050565b60006060830160008301518482036000860152610771828261070c565b91505060208301516107866020860182610745565b5060408301516107996040860182610745565b508091505092915050565b60006107b08383610754565b905092915050565b6000602082019050919050565b60006107d0826106cf565b6107da81856106da565b9350836020820285016107ec856106eb565b8060005b85811015610828578484038952815161080985826107a4565b9450610814836107b8565b925060208a019950506001810190506107f0565b50829750879550505050505092915050565b6000602082019050818103600083015261085481846107c5565b905092915050565b60008060408385031215610873576108726103d8565b5b600083013567ffffffffffffffff811115610891576108906103dd565b5b61089d858286016104fa565b925050602083013567ffffffffffffffff8111156108be576108bd6103dd565b5b6108ca858286016104fa565b9150509250929050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b6000600282049050600182168061091b57607f821691505b60208210810361092e5761092d6108d4565b5b50919050565b600081905092915050565b600061094a826105ba565b6109548185610934565b93506109648185602086016105d6565b80840191505092915050565b600061097c828461093f565b915081905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b60006109c182610528565b91506109cc83610528565b92508282019050808211156109e4576109e3610987565b5b92915050565b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b600060088302610a4c7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82610a0f565b610a568683610a0f565b95508019841693508086168417925050509392505050565b6000819050919050565b6000610a93610a8e610a8984610528565b610a6e565b610528565b9050919050565b6000819050919050565b610aad83610a78565b610ac1610ab982610a9a565b848454610a1c565b825550505050565b600090565b610ad6610ac9565b610ae1818484610aa4565b505050565b5b81811015610b0557610afa600082610ace565b600181019050610ae7565b5050565b601f821115610b4a57610b1b816109ea565b610b24846109ff565b81016020851015610b33578190505b610b47610b3f856109ff565b830182610ae6565b50505b505050565b600082821c905092915050565b6000610b6d60001984600802610b4f565b1980831691505092915050565b6000610b868383610b5c565b9150826002028217905092915050565b610b9f826105ba565b67ffffffffffffffff811115610bb857610bb76103fd565b5b610bc28254610903565b610bcd828285610b09565b600060209050601f831160018114610c005760008415610bee578287015190505b610bf88582610b7a565b865550610c60565b601f198416610c0e866109ea565b60005b82811015610c3657848901518255600182019150602085019450602081019050610c11565b86831015610c535784890151610c4f601f891682610b5c565b8355505b6001600288020188555050505b505050505050565b60006080820190508181036000830152610c828187610600565b90508181036020830152610c968186610600565b9050610ca56040830185610639565b610cb26060830184610639565b9594505050505056fea26469706673582212203ed7a61cffdefcf02560deca9109be79a15416a1df424bc78a40ab26ec88eac564736f6c634300081c0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDVERSION = "addVersion";

    public static final String FUNC_GETVERSIONHISTORY = "getVersionHistory";

    public static final String FUNC_VERSIONS = "versions";

    public static final Event VERSIONADDED_EVENT = new Event("VersionAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected VersionManager(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected VersionManager(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected VersionManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected VersionManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<VersionAddedEventResponse> getVersionAddedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VERSIONADDED_EVENT, transactionReceipt);
        ArrayList<VersionAddedEventResponse> responses = new ArrayList<VersionAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VersionAddedEventResponse typedResponse = new VersionAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.fileId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.cid = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VersionAddedEventResponse getVersionAddedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VERSIONADDED_EVENT, log);
        VersionAddedEventResponse typedResponse = new VersionAddedEventResponse();
        typedResponse.log = log;
        typedResponse.fileId = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.cid = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        return typedResponse;
    }

    public Flowable<VersionAddedEventResponse> versionAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getVersionAddedEventFromLog(log));
    }

    public Flowable<VersionAddedEventResponse> versionAddedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VERSIONADDED_EVENT));
        return versionAddedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addVersion(String fileId, String cid) {
        final Function function = new Function(
                FUNC_ADDVERSION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId), 
                new org.web3j.abi.datatypes.Utf8String(cid)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getVersionHistory(String fileId) {
        final Function function = new Function(FUNC_GETVERSIONHISTORY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Version>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>> versions(String param0,
            BigInteger param1) {
        final Function function = new Function(FUNC_VERSIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>>(function,
                new Callable<Tuple3<String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    @Deprecated
    public static VersionManager load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new VersionManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static VersionManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new VersionManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static VersionManager load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new VersionManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static VersionManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new VersionManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<VersionManager> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VersionManager.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<VersionManager> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VersionManager.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<VersionManager> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VersionManager.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<VersionManager> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VersionManager.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class Version extends DynamicStruct {
        public String cid;

        public BigInteger version;

        public BigInteger timestamp;

        public Version(String cid, BigInteger version, BigInteger timestamp) {
            super(new org.web3j.abi.datatypes.Utf8String(cid), 
                    new org.web3j.abi.datatypes.generated.Uint256(version), 
                    new org.web3j.abi.datatypes.generated.Uint256(timestamp));
            this.cid = cid;
            this.version = version;
            this.timestamp = timestamp;
        }

        public Version(Utf8String cid, Uint256 version, Uint256 timestamp) {
            super(cid, version, timestamp);
            this.cid = cid.getValue();
            this.version = version.getValue();
            this.timestamp = timestamp.getValue();
        }
    }

    public static class VersionAddedEventResponse extends BaseEventResponse {
        public String fileId;

        public String cid;

        public BigInteger version;

        public BigInteger timestamp;
    }
}
