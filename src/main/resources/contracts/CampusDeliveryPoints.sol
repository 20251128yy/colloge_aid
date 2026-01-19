// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract CampusDeliveryPoints {
    // 事件定义
    event PointTransfer(
        uint256 indexed taskId,
        uint256 indexed requesterId,
        uint256 indexed delivererId,
        uint256 amount,
        uint256 timestamp
    );

    // 积分流转记录结构体
    struct PointTransferRecord {
        uint256 taskId;
        uint256 requesterId;
        uint256 delivererId;
        uint256 amount;
        uint256 timestamp;
    }

    // 管理员地址
    address public admin;

    // 积分流转记录列表
    PointTransferRecord[] public transferHistory;

    // 构造函数
    constructor() {
        admin = msg.sender;
    }

    // 积分转账函数
    function transferPoints(
        uint256 taskId,
        uint256 requesterId,
        uint256 delivererId,
        uint256 amount
    ) public onlyAdmin returns (bool) {
        require(amount > 0, "Amount must be greater than 0");

        // 创建流转记录
        PointTransferRecord memory record = PointTransferRecord({
            taskId: taskId,
            requesterId: requesterId,
            delivererId: delivererId,
            amount: amount,
            timestamp: block.timestamp
        });

        // 保存记录
        transferHistory.push(record);

        // 触发事件
        emit PointTransfer(
            taskId,
            requesterId,
            delivererId,
            amount,
            block.timestamp
        );

        return true;
    }

    // 获取指定任务的流转记录
    function getHistory(uint256 taskId) public view returns (PointTransferRecord[] memory) {
        uint256 count = 0;
        // 先计算符合条件的记录数量
        for (uint256 i = 0; i < transferHistory.length; i++) {
            if (transferHistory[i].taskId == taskId) {
                count++;
            }
        }

        // 创建结果数组
        PointTransferRecord[] memory result = new PointTransferRecord[](count);
        uint256 index = 0;
        // 填充结果数组
        for (uint256 i = 0; i < transferHistory.length; i++) {
            if (transferHistory[i].taskId == taskId) {
                result[index] = transferHistory[i];
                index++;
            }
        }

        return result;
    }

    // 获取所有流转记录
    function getAllHistory() public view onlyAdmin returns (PointTransferRecord[] memory) {
        return transferHistory;
    }

    // 获取记录总数
    function getHistoryCount() public view returns (uint256) {
        return transferHistory.length;
    }

    // 管理员权限修饰器
    modifier onlyAdmin() {
        require(msg.sender == admin, "Only admin can call this function");
        _;
    }
}
