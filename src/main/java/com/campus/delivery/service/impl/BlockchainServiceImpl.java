package com.campus.delivery.service.impl;

import com.campus.delivery.entity.PointTransaction;
import com.campus.delivery.repository.PointTransactionRepository;
import com.campus.delivery.service.BlockchainService;
//import jakarta.annotation.PostConstruct;
//import org.fisco.bcos.sdk.BcosSDK;
//import org.fisco.bcos.sdk.client.Client;
//import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
//import org.fisco.bcos.sdk.transaction.Contract;
//import org.fisco.bcos.sdk.transaction.manager.TransactionManager;
//import org.fisco.bcos.sdk.transaction.processor.TransactionProcessorFactory;
//import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
//import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlockchainServiceImpl implements BlockchainService {
    // 智能合约地址A
    @Value("${blockchain.contract.address}")
    private String contractAddress;

    // 智能合约ABI（建议配置文件中存放JSON格式的ABI字符串）
    @Value("${blockchain.contract.abi}")
    private String contractAbi;

    // 智能合约二进制（部署时用，调用时可选）
    @Value("${blockchain.contract.bin:#{null}}")
    private String contractBin;

    // FISCO BCOS SDK配置文件路径（建议配置在application.yml中）
    @Value("${blockchain.sdk.config.path:classpath:fisco-sdk/config.toml}")
    private String sdkConfigPath;

    // 群组ID（默认群组1）
    @Value("${blockchain.group.id:1}")
    private Integer groupId;

//    // FISCO BCOS核心实例（全局初始化一次，避免重复创建）
//    private BcosSDK bcosSDK;
//    private Client client;
//    private CryptoKeyPair cryptoKeyPair;
//    private TransactionManager transactionManager;

    @Autowired
    private PointTransactionRepository pointTransactionRepository;

    @Override
    public String transferPoints(Long taskId, Long requesterId, Long delivererId, Integer amount) {
        return "";
    }

    @Override
    public String getHistoryByTaskId(Long taskId) {
        return "";
    }

    @Override
    public String getAllHistory() {
        return "";
    }

    @Override
    public boolean uploadPointTransaction(Long transactionId) {
        return false;
    }

    /**
     * 【关键优化】使用@PostConstruct注解，服务启动时初始化一次区块链连接
     * 避免每次调用方法都重复初始化，提升性能
     */

//    @PostConstruct
//    private void initBlockchain() {
//        try {
//            // 1. 加载SDK配置并创建BcosSDK实例（config.toml需放在resources/fisco-sdk/目录下）
//            bcosSDK = BcosSDK.build(sdkConfigPath);
//
//            // 2. 获取指定群组的客户端
//            client = bcosSDK.getClient(groupId);
//
//            // 3. 获取加密密钥对（默认加载SDK配置中的默认账户，也可手动加载密钥文件）
//            // 方式1：使用SDK配置的默认密钥对（推荐）
//            cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
//            // 方式2：手动加载pem格式密钥文件（按需替换）
//            // cryptoKeyPair = client.getCryptoSuite().loadKeyPair("classpath:fisco-sdk/accounts/0x1234567890.pem", "密码（若无则传null）");
//
//            // 4. 【核心修改】2.9.1版本创建TransactionManager的正确方式
//            // 替换原有的TransactionManagerFactory
//            transactionManager = TransactionProcessorFactory.createTransactionManager(
//                    client,          // BCOS客户端实例
//                    cryptoKeyPair    // 签名用的密钥对
//            );
//
//            System.out.println("区块链连接初始化成功！群组ID：" + groupId + "，合约地址：" + contractAddress);
//        } catch (Exception e) {
//            System.err.println("区块链连接初始化失败：" + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("初始化区块链连接失败，请检查SDK配置和网络连接", e);
//        }
//    }

//    @Override
//    public String transferPoints(Long taskId, Long requesterId, Long delivererId, Integer amount) {
//        try {
//            // 校验初始化是否完成
//            checkBlockchainInit();
//
//            // 1. 加载智能合约（核心：传入ABI、合约地址、TransactionManager）
//            Contract campusDeliveryPoints = Contract.load(
//                    contractAddress,  // 合约地址
//                    contractAbi,      // ABI字符串（JSON格式）
//                    transactionManager,  // 交易管理器
//                    client.getCryptoSuite()  // 加密套件
//            );
//
//            // 2. 调用合约的transferPoints方法（参数需和合约方法匹配）
//            // 注意：方法名要和合约中完全一致，参数类型需对应（BigInteger对应solidity的uint）
//            TransactionResponse response = campusDeliveryPoints.executeTransaction(
//                    "transferPoints",  // 合约方法名
//                    BigInteger.valueOf(taskId),
//                    BigInteger.valueOf(requesterId),
//                    BigInteger.valueOf(delivererId),
//                    BigInteger.valueOf(amount)
//            );
//
//            // 3. 校验交易是否执行成功
//            if (response.getTransactionReceipt().getStatus() == 0) { // 替换isStatusOK()（2.9.1版本用getStatus()判断，0=成功）
//                return response.getTransactionReceipt().getTransactionHash();
//            } else {
//                throw new ContractException("交易执行失败：" + response.getTransactionReceipt().getMessage());
//            }
//
//        } catch (ContractException e) {
//            throw new RuntimeException("调用智能合约transferPoints失败: " + e.getMessage(), e);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("调用智能合约失败: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public String getHistoryByTaskId(Long taskId) {
//        try {
//            // 校验初始化是否完成
//            checkBlockchainInit();
//
//            // 1. 加载智能合约
//            Contract campusDeliveryPoints = Contract.load(
//                    contractAddress,
//                    contractAbi,
//                    transactionManager,
//                    client.getCryptoSuite()
//            );
//
//            // 2. 调用合约的getHistory方法（查询类方法，无需签名，也可使用call方式）
//            TransactionResponse response = campusDeliveryPoints.executeCall(
//                    "getHistory",  // 合约查询方法名
//                    BigInteger.valueOf(taskId)
//            );
//
//            // 3. 解析返回结果（修复类型转换问题：2.9.1版本返回List<Object>，需手动转换为List<List<BigInteger>>）
//            if (response.getTransactionReceipt().getStatus() == 0) {
//                // 转换逻辑：将List<Object>转换为List<List<BigInteger>>
//                List<Object> rawResult = (List<Object>) response.getReturnObject();
//                List<List<BigInteger>> result = rawResult.stream()
//                        .map(item -> (List<BigInteger>) item)
//                        .collect(Collectors.toList());
//                return result.toString();
//            } else {
//                throw new ContractException("查询失败：" + response.getTransactionReceipt().getMessage());
//            }
//
//        } catch (ContractException e) {
//            throw new RuntimeException("查询智能合约getHistory失败: " + e.getMessage(), e);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("查询智能合约失败: " + e.getMessage(), e);
//        }
//    }

//    @Override
//    public String getAllHistory() {
//        try {
//            // 校验初始化是否完成
//            checkBlockchainInit();
//
//            // 1. 加载智能合约
//            Contract campusDeliveryPoints = Contract.load(
//                    contractAddress,
//                    contractAbi,
//                    transactionManager,
//                    client.getCryptoSuite()
//            );
//
//            // 2. 调用合约的getAllHistory方法
//            TransactionResponse response = campusDeliveryPoints.executeCall("getAllHistory");
//
//            // 3. 解析返回结果（修复类型转换问题）
//            if (response.getTransactionReceipt().getStatus() == 0) {
//                List<Object> rawResult = (List<Object>) response.getReturnObject();
//                List<List<BigInteger>> result = rawResult.stream()
//                        .map(item -> (List<BigInteger>) item)
//                        .collect(Collectors.toList());
//                return result.toString();
//            } else {
//                throw new ContractException("查询失败：" + response.getTransactionReceipt().getMessage());
//            }
//
//        } catch (ContractException e) {
//            throw new RuntimeException("查询智能合约getAllHistory失败: " + e.getMessage(), e);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("查询智能合约失败: " + e.getMessage(), e);
//        }
//    }

    /**
     * 实现积分上链功能，支持重试机制
     * @param transactionId 积分交易ID
     */
//    @Override
//    public boolean uploadPointTransaction(Long transactionId) {
//        // 获取积分交易记录
//        PointTransaction transaction = pointTransactionRepository.findById(transactionId)
//                .orElseThrow(() -> new RuntimeException("积分交易记录不存在：" + transactionId));
//
//        // 如果已经上链成功或者重试次数超过3次，则不再重试
//        if (transaction.getChain_status() == 1 || transaction.getUpload_retry_count() >= 3) {
//            return transaction.getChain_status() == 1;
//        }
//
//        // 增加重试次数
//        transaction.setUpload_retry_count(transaction.getUpload_retry_count() + 1);
//        transaction.setLast_upload_time(new Date());
//
//        try {
//            // 调用区块链服务上链
//            String transactionHash = transferPoints(
//                    transactionId,
//                    transaction.getFrom_user_id(),
//                    transaction.getTo_user_id(),
//                    transaction.getAmount()
//            );
//
//            // 上链成功，更新状态
//            transaction.setChain_status(1); // 已上链
//            transaction.setHash_value(transactionHash);
//            transaction.setChain_error_msg(null);
//            pointTransactionRepository.save(transaction);
//            return true;
//        } catch (Exception e) {
//            // 上链失败，更新状态和失败原因
//            transaction.setChain_status(2); // 上链失败
//            transaction.setChain_error_msg(e.getMessage());
//            pointTransactionRepository.save(transaction);
//            return false;
//        }
//    }

    /**
     * 校验区块链连接是否初始化完成
     */

//    private void checkBlockchainInit() {
//        if (transactionManager == null || client == null) {
//            throw new RuntimeException("区块链连接未初始化，请检查SDK配置或网络连接");
//        }
//    }
}
