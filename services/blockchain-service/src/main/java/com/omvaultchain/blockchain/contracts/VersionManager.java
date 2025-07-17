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
import org.web3j.abi.datatypes.Bool;
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
    public static final String BINARY = "0x6080604052348015600f57600080fd5b50611d878061001f6000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c8063879c30c611610066578063879c30c61461018157806397d655ea146101b35780639a00e589146101cf578063a9910054146101eb578063f5ba8b37146102075761009e565b8063122f4fba146100a357806313993b09146100d3578063223c3bb9146100ef57806372a8cff91461011f57806373d7bfb414610151575b600080fd5b6100bd60048036038101906100b89190611214565b610237565b6040516100ca91906112dc565b60405180910390f35b6100ed60048036038101906100e89190611334565b6103e5565b005b61010960048036038101906101049190611214565b6104ee565b604051610116919061139f565b60405180910390f35b61013960048036038101906101349190611334565b610518565b604051610148939291906113ba565b60405180910390f35b61016b600480360381019061016691906113f8565b610669565b6040516101789190611482565b60405180910390f35b61019b60048036038101906101969190611214565b610956565b6040516101aa939291906113ba565b60405180910390f35b6101cd60048036038101906101c89190611334565b610ae4565b005b6101e960048036038101906101e4919061149d565b610d6c565b005b61020560048036038101906102009190611214565b610f26565b005b610221600480360381019061021c9190611334565b611073565b60405161022e9190611482565b60405180910390f35b6060600060028360405161024b9190611551565b908152602001604051809103902060009054906101000a900460ff1690506001600381111561027d5761027c611568565b5b8160038111156102905761028f611568565b5b036102d3576040518060400160405280600781526020017f63757272656e74000000000000000000000000000000000000000000000000008152509150506103e0565b600260038111156102e7576102e6611568565b5b8160038111156102fa576102f9611568565b5b0361033d576040518060400160405280600781526020017f64656c65746564000000000000000000000000000000000000000000000000008152509150506103e0565b6003808111156103505761034f611568565b5b81600381111561036357610362611568565b5b036103a6576040518060400160405280600b81526020017f726f6c6c65645f6261636b0000000000000000000000000000000000000000008152509150506103e0565b6040518060400160405280600481526020017f6e6f6e65000000000000000000000000000000000000000000000000000000008152509150505b919050565b60008111801561041657506000826040516104009190611551565b9081526020016040518091039020805490508111155b610455576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161044c906115e3565b60405180910390fd5b60016003836040516104679190611551565b9081526020016040518091039020600083815260200190815260200160002060006101000a81548160ff021916908315150217905550816040516104ab9190611551565b60405180910390207f1603631857b6fa1d987e7b70086e41a680a0dc414c994c308e6055a0ff5537e8826040516104e2919061139f565b60405180910390a25050565b600080826040516104ff9190611551565b9081526020016040518091039020805490509050919050565b606060008060008560405161052d9190611551565b9081526020016040518091039020805490508410610580576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016105779061164f565b60405180910390fd5b600080866040516105919190611551565b908152602001604051809103902085815481106105b1576105b061166f565b5b9060005260206000209060030201905080600001816001015482600201548280546105db906116cd565b80601f0160208091040260200160405190810160405280929190818152602001828054610607906116cd565b80156106545780601f1061062957610100808354040283529160200191610654565b820191906000526020600020905b81548152906001019060200180831161063757829003601f168201915b50505050509250935093509350509250925092565b6000808311801561069b57506000846040516106859190611551565b9081526020016040518091039020805490508311155b6106da576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106d19061174a565b60405180910390fd5b60008211801561070b57506000846040516106f59190611551565b9081526020016040518091039020805490508211155b61074a576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610741906117b6565b60405180910390fd5b6000808560405161075b9190611551565b90815260200160405180910390206001856107769190611805565b815481106107875761078661166f565b5b906000526020600020906003020160000180546107a3906116cd565b80601f01602080910402602001604051908101604052809291908181526020018280546107cf906116cd565b801561081c5780601f106107f15761010080835404028352916020019161081c565b820191906000526020600020905b8154815290600101906020018083116107ff57829003601f168201915b50505050509050600080866040516108349190611551565b908152602001604051809103902060018561084f9190611805565b815481106108605761085f61166f565b5b9060005260206000209060030201600001805461087c906116cd565b80601f01602080910402602001604051908101604052809291908181526020018280546108a8906116cd565b80156108f55780601f106108ca576101008083540402835291602001916108f5565b820191906000526020600020905b8154815290600101906020018083116108d857829003601f168201915b505050505090508060405160200161090d9190611551565b60405160208183030381529060405280519060200120826040516020016109349190611551565b6040516020818303038152906040528051906020012014925050509392505050565b6060600080600060018560405161096d9190611551565b90815260200160405180910390205490506000811180156109af57506000856040516109999190611551565b9081526020016040518091039020805490508111155b6109ee576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016109e590611885565b60405180910390fd5b600080866040516109ff9190611551565b9081526020016040518091039020600183610a1a9190611805565b81548110610a2b57610a2a61166f565b5b906000526020600020906003020190508060000181600101548260020154828054610a55906116cd565b80601f0160208091040260200160405190810160405280929190818152602001828054610a81906116cd565b8015610ace5780601f10610aa357610100808354040283529160200191610ace565b820191906000526020600020905b815481529060010190602001808311610ab157829003601f168201915b5050505050925094509450945050509193909250565b600081118015610b155750600082604051610aff9190611551565b9081526020016040518091039020805490508111155b610b54576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b4b906115e3565b60405180910390fd5b60026003811115610b6857610b67611568565b5b600283604051610b789190611551565b908152602001604051809103902060009054906101000a900460ff166003811115610ba657610ba5611568565b5b03610be6576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610bdd906118f1565b60405180910390fd5b600382604051610bf69190611551565b9081526020016040518091039020600082815260200190815260200160002060009054906101000a900460ff1615610c63576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c5a9061195d565b60405180910390fd5b80600183604051610c749190611551565b9081526020016040518091039020819055506003600283604051610c989190611551565b908152602001604051809103902060006101000a81548160ff02191690836003811115610cc857610cc7611568565b5b021790555081604051610cdb9190611551565b60405180910390207f7fd168c0b25418773a9e19bdd04e21046dc67ca1b6bd23bbaee2b7327fca267f82604051610d12919061139f565b60405180910390a281604051610d289190611551565b60405180910390207f5e173101f20d027ef80eb95453eb52b552fc87c7b6d11f68d67f9c5c75919e256003604051610d6091906119c5565b60405180910390a25050565b60006001600084604051610d809190611551565b908152602001604051809103902080549050610d9c91906119e0565b9050600083604051610dae9190611551565b908152602001604051809103902060405180606001604052808481526020018381526020014281525090806001815401808255809150506001900390600052602060002090600302016000909190919091506000820151816000019081610e159190611bc0565b506020820151816001015560408201518160020155505080600184604051610e3d9190611551565b9081526020016040518091039020819055506001600284604051610e619190611551565b908152602001604051809103902060006101000a81548160ff02191690836003811115610e9157610e90611568565b5b02179055507f809733ae1220f7032288ee0ad1846c855a21c243208052b847b01b90dc7ad05b83838342604051610ecb9493929190611c92565b60405180910390a182604051610ee19190611551565b60405180910390207f5e173101f20d027ef80eb95453eb52b552fc87c7b6d11f68d67f9c5c75919e256001604051610f1991906119c5565b60405180910390a2505050565b60026003811115610f3a57610f39611568565b5b600282604051610f4a9190611551565b908152602001604051809103902060009054906101000a900460ff166003811115610f7857610f77611568565b5b03610fb8576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610faf90611d31565b60405180910390fd5b60028082604051610fc99190611551565b908152602001604051809103902060006101000a81548160ff02191690836003811115610ff957610ff8611568565b5b021790555060006001826040516110109190611551565b908152602001604051809103902081905550806040516110309190611551565b60405180910390207f5e173101f20d027ef80eb95453eb52b552fc87c7b6d11f68d67f9c5c75919e25600260405161106891906119c5565b60405180910390a250565b60006003836040516110859190611551565b9081526020016040518091039020600083815260200190815260200160002060009054906101000a900460ff16905092915050565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b611121826110d8565b810181811067ffffffffffffffff821117156111405761113f6110e9565b5b80604052505050565b60006111536110ba565b905061115f8282611118565b919050565b600067ffffffffffffffff82111561117f5761117e6110e9565b5b611188826110d8565b9050602081019050919050565b82818337600083830152505050565b60006111b76111b284611164565b611149565b9050828152602081018484840111156111d3576111d26110d3565b5b6111de848285611195565b509392505050565b600082601f8301126111fb576111fa6110ce565b5b813561120b8482602086016111a4565b91505092915050565b60006020828403121561122a576112296110c4565b5b600082013567ffffffffffffffff811115611248576112476110c9565b5b611254848285016111e6565b91505092915050565b600081519050919050565b600082825260208201905092915050565b60005b8381101561129757808201518184015260208101905061127c565b60008484015250505050565b60006112ae8261125d565b6112b88185611268565b93506112c8818560208601611279565b6112d1816110d8565b840191505092915050565b600060208201905081810360008301526112f681846112a3565b905092915050565b6000819050919050565b611311816112fe565b811461131c57600080fd5b50565b60008135905061132e81611308565b92915050565b6000806040838503121561134b5761134a6110c4565b5b600083013567ffffffffffffffff811115611369576113686110c9565b5b611375858286016111e6565b92505060206113868582860161131f565b9150509250929050565b611399816112fe565b82525050565b60006020820190506113b46000830184611390565b92915050565b600060608201905081810360008301526113d481866112a3565b90506113e36020830185611390565b6113f06040830184611390565b949350505050565b600080600060608486031215611411576114106110c4565b5b600084013567ffffffffffffffff81111561142f5761142e6110c9565b5b61143b868287016111e6565b935050602061144c8682870161131f565b925050604061145d8682870161131f565b9150509250925092565b60008115159050919050565b61147c81611467565b82525050565b60006020820190506114976000830184611473565b92915050565b600080604083850312156114b4576114b36110c4565b5b600083013567ffffffffffffffff8111156114d2576114d16110c9565b5b6114de858286016111e6565b925050602083013567ffffffffffffffff8111156114ff576114fe6110c9565b5b61150b858286016111e6565b9150509250929050565b600081905092915050565b600061152b8261125d565b6115358185611515565b9350611545818560208601611279565b80840191505092915050565b600061155d8284611520565b915081905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602160045260246000fd5b7f496e76616c69642076657273696f6e0000000000000000000000000000000000600082015250565b60006115cd600f83611268565b91506115d882611597565b602082019050919050565b600060208201905081810360008301526115fc816115c0565b9050919050565b7f496e646578206f7574206f6620626f756e647300000000000000000000000000600082015250565b6000611639601383611268565b915061164482611603565b602082019050919050565b600060208201905081810360008301526116688161162c565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b600060028204905060018216806116e557607f821691505b6020821081036116f8576116f761169e565b5b50919050565b7f496e76616c69642076657273696f6e3100000000000000000000000000000000600082015250565b6000611734601083611268565b915061173f826116fe565b602082019050919050565b6000602082019050818103600083015261176381611727565b9050919050565b7f496e76616c69642076657273696f6e3200000000000000000000000000000000600082015250565b60006117a0601083611268565b91506117ab8261176a565b602082019050919050565b600060208201905081810360008301526117cf81611793565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000611810826112fe565b915061181b836112fe565b9250828203905081811115611833576118326117d6565b5b92915050565b7f4e6f2063757272656e742076657273696f6e0000000000000000000000000000600082015250565b600061186f601283611268565b915061187a82611839565b602082019050919050565b6000602082019050818103600083015261189e81611862565b9050919050565b7f46696c652069732064656c657465640000000000000000000000000000000000600082015250565b60006118db600f83611268565b91506118e6826118a5565b602082019050919050565b6000602082019050818103600083015261190a816118ce565b9050919050565b7f56657273696f6e206973206c6f636b6564000000000000000000000000000000600082015250565b6000611947601183611268565b915061195282611911565b602082019050919050565b600060208201905081810360008301526119768161193a565b9050919050565b6004811061198e5761198d611568565b5b50565b600081905061199f8261197d565b919050565b60006119af82611991565b9050919050565b6119bf816119a4565b82525050565b60006020820190506119da60008301846119b6565b92915050565b60006119eb826112fe565b91506119f6836112fe565b9250828201905080821115611a0e57611a0d6117d6565b5b92915050565b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b600060088302611a767fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82611a39565b611a808683611a39565b95508019841693508086168417925050509392505050565b6000819050919050565b6000611abd611ab8611ab3846112fe565b611a98565b6112fe565b9050919050565b6000819050919050565b611ad783611aa2565b611aeb611ae382611ac4565b848454611a46565b825550505050565b600090565b611b00611af3565b611b0b818484611ace565b505050565b5b81811015611b2f57611b24600082611af8565b600181019050611b11565b5050565b601f821115611b7457611b4581611a14565b611b4e84611a29565b81016020851015611b5d578190505b611b71611b6985611a29565b830182611b10565b50505b505050565b600082821c905092915050565b6000611b9760001984600802611b79565b1980831691505092915050565b6000611bb08383611b86565b9150826002028217905092915050565b611bc98261125d565b67ffffffffffffffff811115611be257611be16110e9565b5b611bec82546116cd565b611bf7828285611b33565b600060209050601f831160018114611c2a5760008415611c18578287015190505b611c228582611ba4565b865550611c8a565b601f198416611c3886611a14565b60005b82811015611c6057848901518255600182019150602085019450602081019050611c3b565b86831015611c7d5784890151611c79601f891682611b86565b8355505b6001600288020188555050505b505050505050565b60006080820190508181036000830152611cac81876112a3565b90508181036020830152611cc081866112a3565b9050611ccf6040830185611390565b611cdc6060830184611390565b95945050505050565b7f46696c6520616c72656164792064656c65746564000000000000000000000000600082015250565b6000611d1b601483611268565b9150611d2682611ce5565b602082019050919050565b60006020820190508181036000830152611d4a81611d0e565b905091905056fea26469706673582212200e3024d6dd31c64b30c3d4f47bd50ceec05a1ccbd41506a1f7e39cb453022c3c64736f6c634300081c0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDVERSION = "addVersion";

    public static final String FUNC_COMPAREVERSIONS = "compareVersions";

    public static final String FUNC_DELETEFILE = "deleteFile";

    public static final String FUNC_GETCURRENTVERSION = "getCurrentVersion";

    public static final String FUNC_GETFILESTATUS = "getFileStatus";

    public static final String FUNC_GETVERSIONAT = "getVersionAt";

    public static final String FUNC_GETVERSIONCOUNT = "getVersionCount";

    public static final String FUNC_ISVERSIONLOCKED = "isVersionLocked";

    public static final String FUNC_LOCKVERSION = "lockVersion";

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

    public static final Event VERSIONLOCKED_EVENT = new Event("VersionLocked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Uint256>() {}));
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

    public static List<VersionLockedEventResponse> getVersionLockedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VERSIONLOCKED_EVENT, transactionReceipt);
        ArrayList<VersionLockedEventResponse> responses = new ArrayList<VersionLockedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VersionLockedEventResponse typedResponse = new VersionLockedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.fileId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VersionLockedEventResponse getVersionLockedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VERSIONLOCKED_EVENT, log);
        VersionLockedEventResponse typedResponse = new VersionLockedEventResponse();
        typedResponse.log = log;
        typedResponse.fileId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.version = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<VersionLockedEventResponse> versionLockedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getVersionLockedEventFromLog(log));
    }

    public Flowable<VersionLockedEventResponse> versionLockedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VERSIONLOCKED_EVENT));
        return versionLockedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addVersion(String fileId, String cid) {
        final Function function = new Function(
                FUNC_ADDVERSION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId), 
                new org.web3j.abi.datatypes.Utf8String(cid)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> compareVersions(String fileId, BigInteger version1,
            BigInteger version2) {
        final Function function = new Function(FUNC_COMPAREVERSIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId), 
                new org.web3j.abi.datatypes.generated.Uint256(version1), 
                new org.web3j.abi.datatypes.generated.Uint256(version2)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
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

    public RemoteFunctionCall<Boolean> isVersionLocked(String fileId, BigInteger versionNumber) {
        final Function function = new Function(FUNC_ISVERSIONLOCKED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId), 
                new org.web3j.abi.datatypes.generated.Uint256(versionNumber)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> lockVersion(String fileId,
            BigInteger versionNumber) {
        final Function function = new Function(
                FUNC_LOCKVERSION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(fileId), 
                new org.web3j.abi.datatypes.generated.Uint256(versionNumber)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public static class VersionLockedEventResponse extends BaseEventResponse {
        public byte[] fileId;

        public BigInteger version;
    }
}
