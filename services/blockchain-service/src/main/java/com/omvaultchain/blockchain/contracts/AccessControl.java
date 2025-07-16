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
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
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
public class AccessControl extends Contract {
    public static final String BINARY = "0x6080604052348015600f57600080fd5b50610b9f8061001f6000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80635d98dde814610046578063dd1b91de14610062578063dfe0b5fe14610094575b600080fd5b610060600480360381019061005b91906105c0565b6100b0565b005b61007c6004803603810190610077919061064b565b6101ae565b60405161008b9392919061075a565b60405180910390f35b6100ae60048036038101906100a9919061064b565b6102f0565b005b6040518060600160405280600115158152602001428152602001828152506000846040516100de91906107d4565b908152602001604051809103902060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548160ff02191690831515021790555060208201518160010155604082015181600201908161016891906109f7565b509050507fe2225306a5dd7efe810fb968ac1043ce3cbf47726b578f9aca6093785b22cee0838342846040516101a19493929190610ad8565b60405180910390a1505050565b6000806060600080866040516101c491906107d4565b908152602001604051809103902060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206040518060600160405290816000820160009054906101000a900460ff161515151581526020016001820154815260200160028201805461024f9061081a565b80601f016020809104026020016040519081016040528092919081815260200182805461027b9061081a565b80156102c85780601f1061029d576101008083540402835291602001916102c8565b820191906000526020600020905b8154815290600101906020018083116102ab57829003601f168201915b5050505050815250509050806000015181602001518260400151935093509350509250925092565b6000808360405161030191906107d4565b908152602001604051809103902060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548160ff0219169083151502179055504260008360405161037791906107d4565b908152602001604051809103902060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600101819055507ff7eb47ecbc62e098414c8565b83461841e3ae69f64ad0da8609abefecaec86ad8282426040516103fc93929190610b2b565b60405180910390a15050565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b61046f82610426565b810181811067ffffffffffffffff8211171561048e5761048d610437565b5b80604052505050565b60006104a1610408565b90506104ad8282610466565b919050565b600067ffffffffffffffff8211156104cd576104cc610437565b5b6104d682610426565b9050602081019050919050565b82818337600083830152505050565b6000610505610500846104b2565b610497565b90508281526020810184848401111561052157610520610421565b5b61052c8482856104e3565b509392505050565b600082601f8301126105495761054861041c565b5b81356105598482602086016104f2565b91505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061058d82610562565b9050919050565b61059d81610582565b81146105a857600080fd5b50565b6000813590506105ba81610594565b92915050565b6000806000606084860312156105d9576105d8610412565b5b600084013567ffffffffffffffff8111156105f7576105f6610417565b5b61060386828701610534565b9350506020610614868287016105ab565b925050604084013567ffffffffffffffff81111561063557610634610417565b5b61064186828701610534565b9150509250925092565b6000806040838503121561066257610661610412565b5b600083013567ffffffffffffffff8111156106805761067f610417565b5b61068c85828601610534565b925050602061069d858286016105ab565b9150509250929050565b60008115159050919050565b6106bc816106a7565b82525050565b6000819050919050565b6106d5816106c2565b82525050565b600081519050919050565b600082825260208201905092915050565b60005b838110156107155780820151818401526020810190506106fa565b60008484015250505050565b600061072c826106db565b61073681856106e6565b93506107468185602086016106f7565b61074f81610426565b840191505092915050565b600060608201905061076f60008301866106b3565b61077c60208301856106cc565b818103604083015261078e8184610721565b9050949350505050565b600081905092915050565b60006107ae826106db565b6107b88185610798565b93506107c88185602086016106f7565b80840191505092915050565b60006107e082846107a3565b915081905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b6000600282049050600182168061083257607f821691505b602082108103610845576108446107eb565b5b50919050565b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b6000600883026108ad7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82610870565b6108b78683610870565b95508019841693508086168417925050509392505050565b6000819050919050565b60006108f46108ef6108ea846106c2565b6108cf565b6106c2565b9050919050565b6000819050919050565b61090e836108d9565b61092261091a826108fb565b84845461087d565b825550505050565b600090565b61093761092a565b610942818484610905565b505050565b5b818110156109665761095b60008261092f565b600181019050610948565b5050565b601f8211156109ab5761097c8161084b565b61098584610860565b81016020851015610994578190505b6109a86109a085610860565b830182610947565b50505b505050565b600082821c905092915050565b60006109ce600019846008026109b0565b1980831691505092915050565b60006109e783836109bd565b9150826002028217905092915050565b610a00826106db565b67ffffffffffffffff811115610a1957610a18610437565b5b610a23825461081a565b610a2e82828561096a565b600060209050601f831160018114610a615760008415610a4f578287015190505b610a5985826109db565b865550610ac1565b601f198416610a6f8661084b565b60005b82811015610a9757848901518255600182019150602085019450602081019050610a72565b86831015610ab45784890151610ab0601f8916826109bd565b8355505b6001600288020188555050505b505050505050565b610ad281610582565b82525050565b60006080820190508181036000830152610af28187610721565b9050610b016020830186610ac9565b610b0e60408301856106cc565b8181036060830152610b208184610721565b905095945050505050565b60006060820190508181036000830152610b458186610721565b9050610b546020830185610ac9565b610b6160408301846106cc565b94935050505056fea2646970667358221220ac99517cc46dbb0974f34056a85a36e06b8d9720a40f0ebdcf2f428e50becb9164736f6c634300081c0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_GETACCESS = "getAccess";

    public static final String FUNC_GRANTACCESS = "grantAccess";

    public static final String FUNC_REVOKEACCESS = "revokeAccess";

    public static final Event ACCESSGRANTED_EVENT = new Event("AccessGranted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event ACCESSREVOKED_EVENT = new Event("AccessRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected AccessControl(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AccessControl(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AccessControl(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AccessControl(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<AccessGrantedEventResponse> getAccessGrantedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCESSGRANTED_EVENT, transactionReceipt);
        ArrayList<AccessGrantedEventResponse> responses = new ArrayList<AccessGrantedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccessGrantedEventResponse typedResponse = new AccessGrantedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.cid = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.user = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.encryptedKey = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccessGrantedEventResponse getAccessGrantedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCESSGRANTED_EVENT, log);
        AccessGrantedEventResponse typedResponse = new AccessGrantedEventResponse();
        typedResponse.log = log;
        typedResponse.cid = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.user = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.encryptedKey = (String) eventValues.getNonIndexedValues().get(3).getValue();
        return typedResponse;
    }

    public Flowable<AccessGrantedEventResponse> accessGrantedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccessGrantedEventFromLog(log));
    }

    public Flowable<AccessGrantedEventResponse> accessGrantedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCESSGRANTED_EVENT));
        return accessGrantedEventFlowable(filter);
    }

    public static List<AccessRevokedEventResponse> getAccessRevokedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCESSREVOKED_EVENT, transactionReceipt);
        ArrayList<AccessRevokedEventResponse> responses = new ArrayList<AccessRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccessRevokedEventResponse typedResponse = new AccessRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.cid = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.user = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccessRevokedEventResponse getAccessRevokedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCESSREVOKED_EVENT, log);
        AccessRevokedEventResponse typedResponse = new AccessRevokedEventResponse();
        typedResponse.log = log;
        typedResponse.cid = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.user = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<AccessRevokedEventResponse> accessRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccessRevokedEventFromLog(log));
    }

    public Flowable<AccessRevokedEventResponse> accessRevokedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCESSREVOKED_EVENT));
        return accessRevokedEventFlowable(filter);
    }

    public RemoteFunctionCall<Tuple3<Boolean, BigInteger, String>> getAccess(String cid,
            String user) {
        final Function function = new Function(FUNC_GETACCESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(cid), 
                new org.web3j.abi.datatypes.Address(160, user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple3<Boolean, BigInteger, String>>(function,
                new Callable<Tuple3<Boolean, BigInteger, String>>() {
                    @Override
                    public Tuple3<Boolean, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<Boolean, BigInteger, String>(
                                (Boolean) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> grantAccess(String cid, String user,
            String encryptedKey) {
        final Function function = new Function(
                FUNC_GRANTACCESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(cid), 
                new org.web3j.abi.datatypes.Address(160, user), 
                new org.web3j.abi.datatypes.Utf8String(encryptedKey)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeAccess(String cid, String user) {
        final Function function = new Function(
                FUNC_REVOKEACCESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(cid), 
                new org.web3j.abi.datatypes.Address(160, user)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static AccessControl load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new AccessControl(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AccessControl load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AccessControl(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AccessControl load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new AccessControl(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AccessControl load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AccessControl(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<AccessControl> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AccessControl.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<AccessControl> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AccessControl.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<AccessControl> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AccessControl.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<AccessControl> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AccessControl.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class AccessGrantedEventResponse extends BaseEventResponse {
        public String cid;

        public String user;

        public BigInteger timestamp;

        public String encryptedKey;
    }

    public static class AccessRevokedEventResponse extends BaseEventResponse {
        public String cid;

        public String user;

        public BigInteger timestamp;
    }
}
