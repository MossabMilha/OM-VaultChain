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
import org.web3j.tuples.generated.Tuple2;
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
    public static final String BINARY = "0x6080604052348015600f57600080fd5b506106da8061001f6000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c806313bd20e214610046578063dfe0b5fe14610077578063f37c215e14610093575b600080fd5b610060600480360381019061005b91906104cc565b6100af565b60405161006e92919061055c565b60405180910390f35b610091600480360381019061008c91906104cc565b610156565b005b6100ad60048036038101906100a891906104cc565b610235565b005b600080600080856040516100c391906105f6565b908152602001604051809103902060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206040518060400160405290816000820160009054906101000a900460ff1615151515815260200160018201548152505090508060000151816020015192509250509250929050565b60405180604001604052806000151581526020014281525060008360405161017e91906105f6565b908152602001604051809103902060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548160ff021916908315150217905550602082015181600101559050507ff7eb47ecbc62e098414c8565b83461841e3ae69f64ad0da8609abefecaec86ad82824260405161022993929190610666565b60405180910390a15050565b60405180604001604052806001151581526020014281525060008360405161025d91906105f6565b908152602001604051809103902060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548160ff021916908315150217905550602082015181600101559050507fcd5010f0cf521705dc993ca35e5b2fd59371fb9516f4c5dab9cd77693bbf92b082824260405161030893929190610666565b60405180910390a15050565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b61037b82610332565b810181811067ffffffffffffffff8211171561039a57610399610343565b5b80604052505050565b60006103ad610314565b90506103b98282610372565b919050565b600067ffffffffffffffff8211156103d9576103d8610343565b5b6103e282610332565b9050602081019050919050565b82818337600083830152505050565b600061041161040c846103be565b6103a3565b90508281526020810184848401111561042d5761042c61032d565b5b6104388482856103ef565b509392505050565b600082601f83011261045557610454610328565b5b81356104658482602086016103fe565b91505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006104998261046e565b9050919050565b6104a98161048e565b81146104b457600080fd5b50565b6000813590506104c6816104a0565b92915050565b600080604083850312156104e3576104e261031e565b5b600083013567ffffffffffffffff81111561050157610500610323565b5b61050d85828601610440565b925050602061051e858286016104b7565b9150509250929050565b60008115159050919050565b61053d81610528565b82525050565b6000819050919050565b61055681610543565b82525050565b60006040820190506105716000830185610534565b61057e602083018461054d565b9392505050565b600081519050919050565b600081905092915050565b60005b838110156105b957808201518184015260208101905061059e565b60008484015250505050565b60006105d082610585565b6105da8185610590565b93506105ea81856020860161059b565b80840191505092915050565b600061060282846105c5565b915081905092915050565b600082825260208201905092915050565b600061062982610585565b610633818561060d565b935061064381856020860161059b565b61064c81610332565b840191505092915050565b6106608161048e565b82525050565b60006060820190508181036000830152610680818661061e565b905061068f6020830185610657565b61069c604083018461054d565b94935050505056fea2646970667358221220d7b79d2c131d52aa5ef85bb21396053010a3c8902a82a237920f6299b87cae1764736f6c634300081c0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_GRANTACCESS = "grantAccess";

    public static final String FUNC_HASACCESS = "hasAccess";

    public static final String FUNC_REVOKEACCESS = "revokeAccess";

    public static final Event ACCESSGRANTED_EVENT = new Event("AccessGranted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
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

    public RemoteFunctionCall<TransactionReceipt> grantAccess(String cid, String user) {
        final Function function = new Function(
                FUNC_GRANTACCESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(cid), 
                new org.web3j.abi.datatypes.Address(160, user)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple2<Boolean, BigInteger>> hasAccess(String cid, String user) {
        final Function function = new Function(FUNC_HASACCESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(cid), 
                new org.web3j.abi.datatypes.Address(160, user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<Boolean, BigInteger>>(function,
                new Callable<Tuple2<Boolean, BigInteger>>() {
                    @Override
                    public Tuple2<Boolean, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<Boolean, BigInteger>(
                                (Boolean) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
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
    }

    public static class AccessRevokedEventResponse extends BaseEventResponse {
        public String cid;

        public String user;

        public BigInteger timestamp;
    }
}
