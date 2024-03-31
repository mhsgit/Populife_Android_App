//package com.populock.manhattan.sdk.command;
//
//import com.populock.manhattan.sdk.util.LogUtil;
//
///**
// * Created by Jerry
// */
//public class CommandUtil {
//
//	/**
//	 * 初始化锁请求
//	 */
//	public static void initLockRequestComplectBlock
//
//	/**
//	 * 获取锁电量
//	 */
////	public static void getBatteryLevel(TransferData transferData) {
////		Command command = new Command(transferData.getLockVersion());
////		command.setCommand(transferData.getCommand());
////		transferData.setTransferData(command.buildCommand());
////		BleService.getBleService().sendCommand(transferData);
////	}
//
//	{
//		if (initReqBlock) {
//
//        [[MHLockHelper shareInstance].bleBlockDict setObject:initReqBlock forKey:KKBLE_initReq];
//		}
//
//		int commandID = [[SLUserConfigTools objectForKey:@ "commandID"]integerValue];
//
//		commandID++;
//
//    [SLUserConfigTools setObject:@(commandID)forKey:@ "commandID"];
//
//		String comandNum = @ "03";
//
//		String keyNum = @ "05";
//
//		String comandString = String.format("%s00%s", comandNum, keyNum);
//
//		Integer specialLength = comandString.length / 2;
//
//		String * comandLength = [String getHexByDecimal:specialLength byteLength:2];
//
//		String * sequenceID = [String getHexByDecimal:commandID byteLength:2];
//
//		String * dataStr = [String stringWithFormat:@
//		"AB00%@0000%@%@", comandLength, sequenceID, comandString];
//
//		__block NSInteger i = 0;
//
//		__block String *valueStr;
//
//
//    [self performSelector:@selector(connectTimeOut:)withObject:KKBLE_initReqTimeOut afterDelay:
//		DEFAULT_CONNECT_TIMEOUT];
//
//		weakify(self)
//
//				[[FzhBluetooth shareInstance]writeValue:
//	dataStr forCharacteristic:[FZSingletonManager shareInstance].GPrint_Chatacter completionBlock:^
//		(CBCharacteristic * characteristic, NSError * error){
//
//		LogUtil.d("初始化锁请求发送成功");
//
//	}
//		returnBlock:^
//		(CBPeripheral * peripheral, CBCharacteristic * characteristic, NSString * returnStr, NSError * error)
//		{
//
//			strongify(self)
//
//			LogUtil.d("初始化锁请求应答数据：%s", returnStr);
//
//        [self saveAckCommandIDString:returnStr];
//
//
//			i++;
//
//
//			if (i >= 3) {
//
//				if (i == 3) {
//
//
//					NSInteger count = returnStr.length;
//
//					NSString * str1 = [returnStr substringWithRange:NSMakeRange(10, count - 10)];
//
//					valueStr = str1;
//
//				} else {
//
//
//					valueStr = [valueStr stringByAppendingString:returnStr];
//				}
//
//				if (i == 6) {
//
//
//					//NSString * str1 = [valueStr substringWithRange:NSMakeRange(0, 32)];
//
////                NSString * str2 = [valueStr substringWithRange:NSMakeRange(32, 32)];
////
////                unsigned char cipher[16] = {0};
////
////                for (int i = 0; i < str2.length; i+= 2) {
////
////                    NSString *strByte = [str2 substringWithRange:NSMakeRange(i, 2)];
////
////                    unsigned long red = strtoul([strByte UTF8String],0,16);
////
////                    Byte b =  (Byte) ((0xff & red) );
////
////                    cipher[i/2+0] = b;
////
////                }
////
////                unsigned char pwdkey[16] = {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a,0x0b,0x0c,0x0d,0x0e,0x0f};
////
////                unsigned char  contentStr[16] = {};
////
////                AESDecrypt(cipher, 16, pwdkey, 16, 0, cipher, contentStr);
////
////                NSData * data = [NSData dataWithBytes:(const void *)contentStr length:sizeof(contentStr)];
////
////                NSString * aes = [FSAES128 convertDataToHexStr:data];
////
////                unsigned char  plain[16] = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08};
////
////                unsigned char encrypt[16] = {};
////
////                AESEncrypt(plain, 16, contentStr, 16, 0, contentStr, encrypt);
////
//					//NSData * encryptData = [NSData dataWithBytes:(const void *)encrypt length:sizeof(encrypt)];
//
//					//NSString * encryptString = [FSAES128 convertDataToHexStr:encryptData];
//
//					//_initLockEncrypt = encryptString;
//
//					//[SLUserConfigTools setObject:aes forKey:@"aesString"];
//
//					//NSString * str3 = [valueStr substringWithRange:NSMakeRange(64, 32)];
//
//					//解密lockKey
//					//[self decyptLockKeyString:str3];
//
//					//NSString * str4 = [valueStr substringWithRange:NSMakeRange(96, 32)];
//
//					//[self decyptAdminPwdString:str4];
//
//					//[self receiverInitLockCommandAck:valueStr];
//
//                [self receiverInitLockVerifySuccessReplayAckComplect:^(BOOL success){
//
//
//						if (success) {
//
//                              [NSObject cancelPreviousPerformRequestsWithTarget:self selector:
//							@selector(connectTimeOut:)object:KKBLE_initReqTimeOut];
//
//
//							BLEBlock initReqBlock = _bleBlockDict[KKBLE_initReq];
//
//							NSDictionary * info = @ {
//								@ "initLockReqRand":valueStr
//							} ;
//
//							if (initReqBlock) {
//
//								initReqBlock(YES, info);
//
//							}
//
//
//						}
//
//
//					}];
//
//
//				}
//
//			}
//
//		}];
//
//	}:(BLEBlock)initReqBlock
//
//	private
//	/**
//	 * 初始化锁
//	 */
//	public static void initLock() {
//
//	}
//}
