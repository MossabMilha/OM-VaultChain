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
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
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
    public static final String BINARY = "0x6080604052348015600f57600080fd5b506116478061001f6000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063879c30c61161005b578063879c30c61461011457806397d655ea146101465780639a00e58914610162578063a99100541461017e5761007d565b8063122f4fba14610082578063223c3bb9146100b257806372a8cff9146100e2575b600080fd5b61009c60048036038101906100979190610cbd565b61019a565b6040516100a99190610d85565b60405180910390f35b6100cc60048036038101906100c79190610cbd565b610348565b6040516100d99190610dc0565b60405180910390f35b6100fc60048036038101906100f79190610e07565b610372565b60405161010b93929190610e63565b60405180910390f35b61012e60048036038101906101299190610cbd565b6104c3565b60405161013d93929190610e63565b60405180910390f35b610160600480360381019061015b9190610e07565b610651565b005b61017c60048036038101906101779190610ea1565b61085c565b005b61019860048036038101906101939190610cbd565b610a16565b005b606060006002836040516101ae9190610f55565b908152602001604051809103902060009054906101000a900460ff169050600160038111156101e0576101df610f6c565b5b8160038111156101f3576101f2610f6c565b5b03610236576040518060400160405280600781526020017f63757272656e7400000000000000000000000000000000000000000000000000815250915050610343565b6002600381111561024a57610249610f6c565b5b81600381111561025d5761025c610f6c565b5b036102a0576040518060400160405280600781526020017f64656c6574656400000000000000000000000000000000000000000000000000815250915050610343565b6003808111156102b3576102b2610f6c565b5b8160038111156102c6576102c5610f6c565b5b03610309576040518060400160405280600b81526020017f726f6c6c65645f6261636b000000000000000000000000000000000000000000815250915050610343565b6040518060400160405280600481526020017f6e6f6e65000000000000000000000000000000000000000000000000000000008152509150505b919050565b600080826040516103599190610f55565b9081526020016040518091039020805490509050919050565b60606000806000856040516103879190610f55565b90815260200160405180910390208054905084106103da576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016103d190610fe7565b60405180910390fd5b600080866040516103eb9190610f55565b9081526020016040518091039020858154811061040b5761040a611007565b5b90600052602060002090600302019050806000018160010154826002015482805461043590611065565b80601f016020809104026020016040519081016040528092919081815260200182805461046190611065565b80156104ae5780601f10610483576101008083540402835291602001916104ae565b820191906000526020600020905b81548152906001019060200180831161049157829003601f168201915b50505050509250935093509350509250925092565b606060008060006001856040516104da9190610f55565b908152602001604051809103902054905060008111801561051c57506000856040516105069190610f55565b9081526020016040518091039020805490508111155b61055b576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610552906110e2565b60405180910390fd5b6000808660405161056c9190610f55565b90815260200160405180910390206001836105879190611131565b8154811061059857610597611007565b5b9060005260206000209060030201905080600001816001015482600201548280546105c290611065565b80601f01602080910402602001604051908101604052809291908181526020018280546105ee90611065565b801561063b5780601f106106105761010080835404028352916020019161063b565b820191906000526020600020905b81548152906001019060200180831161061e57829003601f168201915b5050505050925094509450945050509193909250565b600081118015610682575060008260405161066c9190610f55565b9081526020016040518091039020805490508111155b6106c1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106b8906111b1565b60405180910390fd5b600260038111156106d5576106d4610f6c565b5b6002836040516106e59190610f55565b908152602001604051809103902060009054906101000a900460ff16600381111561071357610712610f6c565b5b03610753576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161074a9061121d565b60405180910390fd5b806001836040516107649190610f55565b90815260200160405180910390208190555060036002836040516107889190610f55565b908152602001604051809103902060006101000a81548160ff021916908360038111156107b8576107b7610f6c565b5b0217905550816040516107cb9190610f55565b60405180910390207f7fd168c0b25418773a9e19bdd04e21046dc67ca1b6bd23bbaee2b7327fca267f826040516108029190610dc0565b60405180910390a2816040516108189190610f55565b60405180910390207f5e173101f20d027ef80eb95453eb52b552fc87c7b6d11f68d67f9c5c75919e2560036040516108509190611285565b60405180910390a25050565b600060016000846040516108709190610f55565b90815260200160405180910390208054905061088c91906112a0565b905060008360405161089e9190610f55565b9081526020016040518091039020604051806060016040528084815260200183815260200142815250908060018154018082558091505060019003906000526020600020906003020160009091909190915060008201518160000190816109059190611480565b50602082015181600101556040820151816002015550508060018460405161092d9190610f55565b90815260200160405180910390208190555060016002846040516109519190610f55565b908152602001604051809103902060006101000a81548160ff0219169083600381111561098157610980610f6c565b5b02179055507f809733ae1220f7032288ee0ad1846c855a21c243208052b847b01b90dc7ad05b838383426040516109bb9493929190611552565b60405180910390a1826040516109d19190610f55565b60405180910390207f5e173101f20d027ef80eb95453eb52b552fc87c7b6d11f68d67f9c5c75919e256001604051610a099190611285565b60405180910390a2505050565b60026003811115610a2a57610a29610f6c565b5b600282604051610a3a9190610f55565b908152602001604051809103902060009054906101000a900460ff166003811115610a6857610a67610f6c565b5b03610aa8576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610a9f906115f1565b60405180910390fd5b60028082604051610ab99190610f55565b908152602001604051809103902060006101000a81548160ff02191690836003811115610ae957610ae8610f6c565b5b02179055506000600182604051610b009190610f55565b90815260200160405180910390208190555080604051610b209190610f55565b60405180910390207f5e173101f20d027ef80eb95453eb52b552fc87c7b6d11f68d67f9c5c75919e256002604051610b589190611285565b60405180910390a250565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b610bca82610b81565b810181811067ffffffffffffffff82111715610be957610be8610b92565b5b80604052505050565b6000610bfc610b63565b9050610c088282610bc1565b919050565b600067ffffffffffffffff821115610c2857610c27610b92565b5b610c3182610b81565b9050602081019050919050565b82818337600083830152505050565b6000610c60610c5b84610c0d565b610bf2565b905082815260208101848484011115610c7c57610c7b610b7c565b5b610c87848285610c3e565b509392505050565b600082601f830112610ca457610ca3610b77565b5b8135610cb4848260208601610c4d565b91505092915050565b600060208284031215610cd357610cd2610b6d565b5b600082013567ffffffffffffffff811115610cf157610cf0610b72565b5b610cfd84828501610c8f565b91505092915050565b600081519050919050565b600082825260208201905092915050565b60005b83811015610d40578082015181840152602081019050610d25565b60008484015250505050565b6000610d5782610d06565b610d618185610d11565b9350610d71818560208601610d22565b610d7a81610b81565b840191505092915050565b60006020820190508181036000830152610d9f8184610d4c565b905092915050565b6000819050919050565b610dba81610da7565b82525050565b6000602082019050610dd56000830184610db1565b92915050565b610de481610da7565b8114610def57600080fd5b50565b600081359050610e0181610ddb565b92915050565b60008060408385031215610e1e57610e1d610b6d565b5b600083013567ffffffffffffffff811115610e3c57610e3b610b72565b5b610e4885828601610c8f565b9250506020610e5985828601610df2565b9150509250929050565b60006060820190508181036000830152610e7d8186610d4c565b9050610e8c6020830185610db1565b610e996040830184610db1565b949350505050565b60008060408385031215610eb857610eb7610b6d565b5b600083013567ffffffffffffffff811115610ed657610ed5610b72565b5b610ee285828601610c8f565b925050602083013567ffffffffffffffff811115610f0357610f02610b72565b5b610f0f85828601610c8f565b9150509250929050565b600081905092915050565b6000610f2f82610d06565b610f398185610f19565b9350610f49818560208601610d22565b80840191505092915050565b6000610f618284610f24565b915081905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602160045260246000fd5b7f496e646578204f7574204f6620426f756e647300000000000000000000000000600082015250565b6000610fd1601383610d11565b9150610fdc82610f9b565b602082019050919050565b6000602082019050818103600083015261100081610fc4565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b6000600282049050600182168061107d57607f821691505b6020821081036110905761108f611036565b5b50919050565b7f4e6f2043757272656e742056657273696f6e0000000000000000000000000000600082015250565b60006110cc601283610d11565b91506110d782611096565b602082019050919050565b600060208201905081810360008301526110fb816110bf565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b600061113c82610da7565b915061114783610da7565b925082820390508181111561115f5761115e611102565b5b92915050565b7f496e76616c69642056657273696f6e0000000000000000000000000000000000600082015250565b600061119b600f83610d11565b91506111a682611165565b602082019050919050565b600060208201905081810360008301526111ca8161118e565b9050919050565b7f46696c652049732044656c657465640000000000000000000000000000000000600082015250565b6000611207600f83610d11565b9150611212826111d1565b602082019050919050565b60006020820190508181036000830152611236816111fa565b9050919050565b6004811061124e5761124d610f6c565b5b50565b600081905061125f8261123d565b919050565b600061126f82611251565b9050919050565b61127f81611264565b82525050565b600060208201905061129a6000830184611276565b92915050565b60006112ab82610da7565b91506112b683610da7565b92508282019050808211156112ce576112cd611102565b5b92915050565b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b6000600883026113367fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff826112f9565b61134086836112f9565b95508019841693508086168417925050509392505050565b6000819050919050565b600061137d61137861137384610da7565b611358565b610da7565b9050919050565b6000819050919050565b61139783611362565b6113ab6113a382611384565b848454611306565b825550505050565b600090565b6113c06113b3565b6113cb81848461138e565b505050565b5b818110156113ef576113e46000826113b8565b6001810190506113d1565b5050565b601f82111561143457611405816112d4565b61140e846112e9565b8101602085101561141d578190505b611431611429856112e9565b8301826113d0565b50505b505050565b600082821c905092915050565b600061145760001984600802611439565b1980831691505092915050565b60006114708383611446565b9150826002028217905092915050565b61148982610d06565b67ffffffffffffffff8111156114a2576114a1610b92565b5b6114ac8254611065565b6114b78282856113f3565b600060209050601f8311600181146114ea57600084156114d8578287015190505b6114e28582611464565b86555061154a565b601f1984166114f8866112d4565b60005b82811015611520578489015182556001820191506020850194506020810190506114fb565b8683101561153d5784890151611539601f891682611446565b8355505b6001600288020188555050505b505050505050565b6000608082019050818103600083015261156c8187610d4c565b905081810360208301526115808186610d4c565b905061158f6040830185610db1565b61159c6060830184610db1565b95945050505050565b7f46696c6520416c72656164792044656c65746564000000000000000000000000600082015250565b60006115db601483610d11565b91506115e6826115a5565b602082019050919050565b6000602082019050818103600083015261160a816115ce565b905091905056fea26469706673582212202680ccabafd05d43bc36507a4408ecc5c19fe620ac8300c936f3b33dd4de437264736f6c634300081c0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDVERSION = "addVersion";

    public static final String FUNC_DELETEFILE = "deleteFile";

    public static final String FUNC_GETCURRENTVERSION = "getCurrentVersion";

    public static final String FUNC_GETFILESTATUS = "getFileStatus";

    public static final String FUNC_GETVERSIONAT = "getVersionAt";

    public static final String FUNC_GETVERSIONCOUNT = "getVersionCount";

    public static final String FUNC_ROLLBACKTOVERSION = "rollbackToVersion";

    public static final Event FILESTATUSCHANGED_EVENT = new Event("FileStatusChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Uint8>() {}));
    ;

    public static final Event ROLLEDBACK_EVENT = new Event("RolledBack", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Uint256>() {}));
    ;

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

    public static List<FileStatusChangedEventResponse> getFileStatusChangedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(FILESTATUSCHANGED_EVENT, transactionReceipt);
        ArrayList<FileStatusChangedEventResponse> responses = new ArrayList<FileStatusChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FileStatusChangedEventResponse typedResponse = new FileStatusChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.fileId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.status = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static FileStatusChangedEventResponse getFileStatusChangedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(FILESTATUSCHANGED_EVENT, log);
        FileStatusChangedEventResponse typedResponse = new FileStatusChangedEventResponse();
        typedResponse.log = log;
        typedResponse.fileId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.status = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<FileStatusChangedEventResponse> fileStatusChangedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getFileStatusChangedEventFromLog(log));
    }

    public Flowable<FileStatusChangedEventResponse> fileStatusChangedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FILESTATUSCHANGED_EVENT));
        return fileStatusChangedEventFlowable(filter);
    }

    public static List<RolledBackEventResponse> getRolledBackEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ROLLEDBACK_EVENT, transactionReceipt);
        ArrayList<RolledBackEventResponse> responses = new ArrayList<RolledBackEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RolledBackEventResponse typedResponse = new RolledBackEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.fileId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.toVersion = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static RolledBackEventResponse getRolledBackEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ROLLEDBACK_EVENT, log);
        RolledBackEventResponse typedResponse = new RolledBackEventResponse();
        typedResponse.log = log;
        typedResponse.fileId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.toVersion = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<RolledBackEventResponse> rolledBackEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getRolledBackEventFromLog(log));
    }

    public Flowable<RolledBackEventResponse> rolledBackEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLLEDBACK_EVENT));
        return rolledBackEventFlowable(filter);
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

    public RemoteFunctionCall<TransactionReceipt> deleteFile(String fileId) {
        final Function function = new Function(
                FUNC_DELETEFILE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>> getCurrentVersion(
            String fileId) {
        final Function function = new Function(FUNC_GETCURRENTVERSION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId)), 
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

    public RemoteFunctionCall<String> getFileStatus(String fileId) {
        final Function function = new Function(FUNC_GETFILESTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>> getVersionAt(String fileId,
            BigInteger index) {
        final Function function = new Function(FUNC_GETVERSIONAT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId), 
                new org.web3j.abi.datatypes.generated.Uint256(index)), 
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

    public RemoteFunctionCall<BigInteger> getVersionCount(String fileId) {
        final Function function = new Function(FUNC_GETVERSIONCOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> rollbackToVersion(String fileId,
            BigInteger versionNumber) {
        final Function function = new Function(
                FUNC_ROLLBACKTOVERSION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId), 
                new org.web3j.abi.datatypes.generated.Uint256(versionNumber)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public static class FileStatusChangedEventResponse extends BaseEventResponse {
        public byte[] fileId;

        public BigInteger status;
    }

    public static class RolledBackEventResponse extends BaseEventResponse {
        public byte[] fileId;

        public BigInteger toVersion;
    }

    public static class VersionAddedEventResponse extends BaseEventResponse {
        public String fileId;

        public String cid;

        public BigInteger version;

        public BigInteger timestamp;
    }
}
