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
import org.web3j.abi.datatypes.DynamicArray;
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
    public static final String BINARY = "0x6080604052348015600f57600080fd5b506118fc8061001f6000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063c9f3c3b91161005b578063c9f3c3b9146100ea578063d655c0d314610106578063dd1b91de14610136578063dfe0b5fe146101685761007d565b806313bd20e2146100825780635d98dde8146100b2578063a30eb435146100ce575b600080fd5b61009c60048036038101906100979190610e09565b610184565b6040516100a99190610e80565b60405180910390f35b6100cc60048036038101906100c79190610e9b565b6101f9565b005b6100e860048036038101906100e391906110cf565b6103e7565b005b61010460048036038101906100ff9190611176565b6104c5565b005b610120600480360381019061011b9190611176565b610725565b60405161012d919061127d565b60405180910390f35b610150600480360381019061014b9190610e09565b6109f7565b60405161015f93929190611337565b60405180910390f35b610182600480360381019061017d9190610e09565b610b39565b005b6000808360405161019591906113b1565b908152602001604051809103902060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16905092915050565b60008360405161020991906113b1565b908152602001604051809103902060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff166102e95760018360405161027991906113b1565b9081526020016040518091039020829080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b60405180606001604052806001151581526020014281526020018281525060008460405161031791906113b1565b908152602001604051809103902060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548160ff0219169083151502179055506020820151816001015560408201518160020190816103a191906115d4565b509050507fe2225306a5dd7efe810fb968ac1043ce3cbf47726b578f9aca6093785b22cee0838342846040516103da94939291906116b5565b60405180910390a1505050565b805182511461042b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161042290611754565b60405180910390fd5b60005b8251811015610483576104768484838151811061044e5761044d611774565b5b602002602001015184848151811061046957610468611774565b5b60200260200101516101f9565b808060010191505061042e565b507f88bce2bf8c74ef0e8d68e281d5d74add9b08383d885848a4b2df7779f29a29c9838351426040516104b8939291906117a3565b60405180910390a1505050565b60006001826040516104d791906113b1565b908152602001604051809103902080548060200260200160405190810160405280929190818152602001828054801561056557602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001906001019080831161051b575b5050505050905060005b81518110156106e7576000808460405161058991906113b1565b908152602001604051809103902060008484815181106105ac576105ab611774565b5b602002602001015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548160ff0219169083151502179055504260008460405161061991906113b1565b9081526020016040518091039020600084848151811061063c5761063b611774565b5b602002602001015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600101819055507ff7eb47ecbc62e098414c8565b83461841e3ae69f64ad0da8609abefecaec86ad838383815181106106ba576106b9611774565b5b6020026020010151426040516106d2939291906117e1565b60405180910390a1808060010191505061056f565b507f46684813f3d42e0e5714590e40cfc55a85256f0437a6116579a0bde77332397a824260405161071992919061181f565b60405180910390a15050565b6060600060018360405161073991906113b1565b90815260200160405180910390208054806020026020016040519081016040528092919081815260200182805480156107c757602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001906001019080831161077d575b505050505090506000805b8251811015610882576000856040516107eb91906113b1565b9081526020016040518091039020600084838151811061080e5761080d611774565b5b602002602001015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16156108755781806108719061187e565b9250505b80806001019150506107d2565b5060008167ffffffffffffffff81111561089f5761089e610c80565b5b6040519080825280602002602001820160405280156108cd5781602001602082028036833780820191505090505b5090506000805b84518110156109ea576000876040516108ed91906113b1565b908152602001604051809103902060008683815181106109105761090f611774565b5b602002602001015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16156109dd5784818151811061097b5761097a611774565b5b602002602001015183838061098f9061187e565b9450815181106109a2576109a1611774565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff16815250505b80806001019150506108d4565b5081945050505050919050565b600080606060008086604051610a0d91906113b1565b908152602001604051809103902060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206040518060600160405290816000820160009054906101000a900460ff1615151515815260200160018201548152602001600282018054610a98906113f7565b80601f0160208091040260200160405190810160405280929190818152602001828054610ac4906113f7565b8015610b115780601f10610ae657610100808354040283529160200191610b11565b820191906000526020600020905b815481529060010190602001808311610af457829003601f168201915b5050505050815250509050806000015181602001518260400151935093509350509250925092565b60008083604051610b4a91906113b1565b908152602001604051809103902060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548160ff02191690831515021790555042600083604051610bc091906113b1565b908152602001604051809103902060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600101819055507ff7eb47ecbc62e098414c8565b83461841e3ae69f64ad0da8609abefecaec86ad828242604051610c45939291906117e1565b60405180910390a15050565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b610cb882610c6f565b810181811067ffffffffffffffff82111715610cd757610cd6610c80565b5b80604052505050565b6000610cea610c51565b9050610cf68282610caf565b919050565b600067ffffffffffffffff821115610d1657610d15610c80565b5b610d1f82610c6f565b9050602081019050919050565b82818337600083830152505050565b6000610d4e610d4984610cfb565b610ce0565b905082815260208101848484011115610d6a57610d69610c6a565b5b610d75848285610d2c565b509392505050565b600082601f830112610d9257610d91610c65565b5b8135610da2848260208601610d3b565b91505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610dd682610dab565b9050919050565b610de681610dcb565b8114610df157600080fd5b50565b600081359050610e0381610ddd565b92915050565b60008060408385031215610e2057610e1f610c5b565b5b600083013567ffffffffffffffff811115610e3e57610e3d610c60565b5b610e4a85828601610d7d565b9250506020610e5b85828601610df4565b9150509250929050565b60008115159050919050565b610e7a81610e65565b82525050565b6000602082019050610e956000830184610e71565b92915050565b600080600060608486031215610eb457610eb3610c5b565b5b600084013567ffffffffffffffff811115610ed257610ed1610c60565b5b610ede86828701610d7d565b9350506020610eef86828701610df4565b925050604084013567ffffffffffffffff811115610f1057610f0f610c60565b5b610f1c86828701610d7d565b9150509250925092565b600067ffffffffffffffff821115610f4157610f40610c80565b5b602082029050602081019050919050565b600080fd5b6000610f6a610f6584610f26565b610ce0565b90508083825260208201905060208402830185811115610f8d57610f8c610f52565b5b835b81811015610fb65780610fa28882610df4565b845260208401935050602081019050610f8f565b5050509392505050565b600082601f830112610fd557610fd4610c65565b5b8135610fe5848260208601610f57565b91505092915050565b600067ffffffffffffffff82111561100957611008610c80565b5b602082029050602081019050919050565b600061102d61102884610fee565b610ce0565b905080838252602082019050602084028301858111156110505761104f610f52565b5b835b8181101561109757803567ffffffffffffffff81111561107557611074610c65565b5b8086016110828982610d7d565b85526020850194505050602081019050611052565b5050509392505050565b600082601f8301126110b6576110b5610c65565b5b81356110c684826020860161101a565b91505092915050565b6000806000606084860312156110e8576110e7610c5b565b5b600084013567ffffffffffffffff81111561110657611105610c60565b5b61111286828701610d7d565b935050602084013567ffffffffffffffff81111561113357611132610c60565b5b61113f86828701610fc0565b925050604084013567ffffffffffffffff8111156111605761115f610c60565b5b61116c868287016110a1565b9150509250925092565b60006020828403121561118c5761118b610c5b565b5b600082013567ffffffffffffffff8111156111aa576111a9610c60565b5b6111b684828501610d7d565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b6111f481610dcb565b82525050565b600061120683836111eb565b60208301905092915050565b6000602082019050919050565b600061122a826111bf565b61123481856111ca565b935061123f836111db565b8060005b8381101561127057815161125788826111fa565b975061126283611212565b925050600181019050611243565b5085935050505092915050565b60006020820190508181036000830152611297818461121f565b905092915050565b6000819050919050565b6112b28161129f565b82525050565b600081519050919050565b600082825260208201905092915050565b60005b838110156112f25780820151818401526020810190506112d7565b60008484015250505050565b6000611309826112b8565b61131381856112c3565b93506113238185602086016112d4565b61132c81610c6f565b840191505092915050565b600060608201905061134c6000830186610e71565b61135960208301856112a9565b818103604083015261136b81846112fe565b9050949350505050565b600081905092915050565b600061138b826112b8565b6113958185611375565b93506113a58185602086016112d4565b80840191505092915050565b60006113bd8284611380565b915081905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b6000600282049050600182168061140f57607f821691505b602082108103611422576114216113c8565b5b50919050565b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b60006008830261148a7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8261144d565b611494868361144d565b95508019841693508086168417925050509392505050565b6000819050919050565b60006114d16114cc6114c78461129f565b6114ac565b61129f565b9050919050565b6000819050919050565b6114eb836114b6565b6114ff6114f7826114d8565b84845461145a565b825550505050565b600090565b611514611507565b61151f8184846114e2565b505050565b5b818110156115435761153860008261150c565b600181019050611525565b5050565b601f8211156115885761155981611428565b6115628461143d565b81016020851015611571578190505b61158561157d8561143d565b830182611524565b50505b505050565b600082821c905092915050565b60006115ab6000198460080261158d565b1980831691505092915050565b60006115c4838361159a565b9150826002028217905092915050565b6115dd826112b8565b67ffffffffffffffff8111156115f6576115f5610c80565b5b61160082546113f7565b61160b828285611547565b600060209050601f83116001811461163e576000841561162c578287015190505b61163685826115b8565b86555061169e565b601f19841661164c86611428565b60005b828110156116745784890151825560018201915060208501945060208101905061164f565b86831015611691578489015161168d601f89168261159a565b8355505b6001600288020188555050505b505050505050565b6116af81610dcb565b82525050565b600060808201905081810360008301526116cf81876112fe565b90506116de60208301866116a6565b6116eb60408301856112a9565b81810360608301526116fd81846112fe565b905095945050505050565b7f4d69736d61746368656420696e707574206c656e677468730000000000000000600082015250565b600061173e6018836112c3565b915061174982611708565b602082019050919050565b6000602082019050818103600083015261176d81611731565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b600060608201905081810360008301526117bd81866112fe565b90506117cc60208301856112a9565b6117d960408301846112a9565b949350505050565b600060608201905081810360008301526117fb81866112fe565b905061180a60208301856116a6565b61181760408301846112a9565b949350505050565b6000604082019050818103600083015261183981856112fe565b905061184860208301846112a9565b9392505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b60006118898261129f565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82036118bb576118ba61184f565b5b60018201905091905056fea26469706673582212200cc5a6262eb28fb0b69dee88dfb9a9e1080a945c95df8ca180443a7945517a6964736f6c634300081c0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_GETACCESS = "getAccess";

    public static final String FUNC_GETACCESSLIST = "getAccessList";

    public static final String FUNC_GRANTACCESS = "grantAccess";

    public static final String FUNC_GRANTMULTIPLEACCESS = "grantMultipleAccess";

    public static final String FUNC_HASACCESS = "hasAccess";

    public static final String FUNC_REVOKEACCESS = "revokeAccess";

    public static final String FUNC_REVOKEALLACCESS = "revokeAllAccess";

    public static final Event ACCESSALLREVOKED_EVENT = new Event("AccessAllRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ACCESSBULKGRANTED_EVENT = new Event("AccessBulkGranted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

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

    public static List<AccessAllRevokedEventResponse> getAccessAllRevokedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCESSALLREVOKED_EVENT, transactionReceipt);
        ArrayList<AccessAllRevokedEventResponse> responses = new ArrayList<AccessAllRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccessAllRevokedEventResponse typedResponse = new AccessAllRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.cid = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccessAllRevokedEventResponse getAccessAllRevokedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCESSALLREVOKED_EVENT, log);
        AccessAllRevokedEventResponse typedResponse = new AccessAllRevokedEventResponse();
        typedResponse.log = log;
        typedResponse.cid = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<AccessAllRevokedEventResponse> accessAllRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccessAllRevokedEventFromLog(log));
    }

    public Flowable<AccessAllRevokedEventResponse> accessAllRevokedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCESSALLREVOKED_EVENT));
        return accessAllRevokedEventFlowable(filter);
    }

    public static List<AccessBulkGrantedEventResponse> getAccessBulkGrantedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCESSBULKGRANTED_EVENT, transactionReceipt);
        ArrayList<AccessBulkGrantedEventResponse> responses = new ArrayList<AccessBulkGrantedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccessBulkGrantedEventResponse typedResponse = new AccessBulkGrantedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.cid = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.count = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AccessBulkGrantedEventResponse getAccessBulkGrantedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ACCESSBULKGRANTED_EVENT, log);
        AccessBulkGrantedEventResponse typedResponse = new AccessBulkGrantedEventResponse();
        typedResponse.log = log;
        typedResponse.cid = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.count = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<AccessBulkGrantedEventResponse> accessBulkGrantedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAccessBulkGrantedEventFromLog(log));
    }

    public Flowable<AccessBulkGrantedEventResponse> accessBulkGrantedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCESSBULKGRANTED_EVENT));
        return accessBulkGrantedEventFlowable(filter);
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

    public RemoteFunctionCall<List> getAccessList(String cid) {
        final Function function = new Function(FUNC_GETACCESSLIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(cid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
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

    public RemoteFunctionCall<TransactionReceipt> grantMultipleAccess(String cid,
            List<String> users, List<String> encryptedKeys) {
        final Function function = new Function(
                FUNC_GRANTMULTIPLEACCESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(cid), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(users, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(encryptedKeys, org.web3j.abi.datatypes.Utf8String.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> hasAccess(String cid, String user) {
        final Function function = new Function(FUNC_HASACCESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(cid), 
                new org.web3j.abi.datatypes.Address(160, user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeAccess(String cid, String user) {
        final Function function = new Function(
                FUNC_REVOKEACCESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(cid), 
                new org.web3j.abi.datatypes.Address(160, user)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeAllAccess(String cid) {
        final Function function = new Function(
                FUNC_REVOKEALLACCESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(cid)), 
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

    public static class AccessAllRevokedEventResponse extends BaseEventResponse {
        public String cid;

        public BigInteger timestamp;
    }

    public static class AccessBulkGrantedEventResponse extends BaseEventResponse {
        public String cid;

        public BigInteger count;

        public BigInteger timestamp;
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
