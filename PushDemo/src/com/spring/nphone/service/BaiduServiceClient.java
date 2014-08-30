package com.spring.nphone.service;

import java.util.List;

import android.util.Log;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.BindInfo;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageResponse;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageResponse;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.channel.model.QueryBindListRequest;
import com.baidu.yun.channel.model.QueryBindListResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.spring.nphone.domain.User;

/**
 * 云推送服务
 * 
 * @author yd
 * 
 */
public class BaiduServiceClient {

	private BaiduServiceClient() {
	}

	public final String TAG = this.getClass().getSimpleName();

	private static Object lock = new Object();

	private static BaiduServiceClient client = null;

	public static BaiduServiceClient getInstance() {
		if (null == client) {
			synchronized (lock) {
				if (null == client) {
					return new BaiduServiceClient();
				}
			}
		}
		return client;
	}

	private String apiKey = "xoW76TAvoI5UioPtoujBNNNE";
	private String secretKey = "ae5hpTDyySuMrbHkF0WPSsbX2XVOaXxu";

	/**
	 * 发送notify通知
	 * 
	 * @param user
	 * @param content
	 */
	public void pushNotification(User user, String content) {

		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
		// 2. 创建BaiduChannelClient对象实例
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
		// 3. 若要了解交互细节，请注册YunLogHandler类
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				Log.i(TAG, event.getMessage());
			}
		});
		try {
			// 4. 创建请求类对象
			// 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
			PushUnicastMessageRequest request = new PushUnicastMessageRequest();
			request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
										// 4:ios 5:wp
			request.setChannelId(user.getChannelId());
			request.setUserId(user.getUserId());

			request.setMessageType(1);
			request.setMessage(content);
			// 5. 调用pushMessage接口
			PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);
			// 6. 认证推送成功
			Log.i(TAG, "push amount : " + response.getSuccessAmount());
		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			e.printStackTrace();
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			Log.i(TAG, String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
		}

	}

	public void pushTagMessage(String tagname, String content) {

		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
		// 2. 创建BaiduChannelClient对象实例
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
		// 3. 若要了解交互细节，请注册YunLogHandler类
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				Log.i(TAG, event.getMessage());
			}
		});
		try {
			// 4. 创建请求类对象
			PushTagMessageRequest request = new PushTagMessageRequest();
			request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
										// 4:ios 5:wp
			request.setTagName("xxxx");
			request.setMessage("Hello Channel");
			// 若要通知，
			// request.setMessageType(1);
			// request.setMessage("{\"title\":\"Notify_title_danbo\",\"description\":\"Notify_description_content\"}");

			// 5. 调用pushMessage接口
			PushTagMessageResponse response = channelClient.pushTagMessage(request);

			// 6. 认证推送成功
			Log.i(TAG, "push amount : " + response.getSuccessAmount());

		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			e.printStackTrace();
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			Log.i(TAG, String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
		}
	}

	/*
	 * @brief 推送广播消息(消息类型为透传，由开发方应用自己来解析消息内容) message_type = 0 (默认为0)
	 */
	public void pushBroadcastMessage(String message) {

		// 1. 设置developer平台的ApiKey/SecretKey
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
		// 2. 创建BaiduChannelClient对象实例
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);

		// 3. 若要了解交互细节，请注册YunLogHandler类
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				Log.i(TAG, event.getMessage());
			}
		});

		try {

			// 4. 创建请求类对象
			PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
			request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
										// 4:ios 5:wp

			request.setMessage("Hello Channel");
			// 若要通知，
			// request.setMessageType(1);
			// request.setMessage("{\"title\":\"Notify_title_danbo\",\"description\":\"Notify_description_content\"}");

			// 5. 调用pushMessage接口
			PushBroadcastMessageResponse response = channelClient.pushBroadcastMessage(request);

			// 6. 认证推送成功
			Log.i(TAG, "push amount : " + response.getSuccessAmount());

		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			e.printStackTrace();
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			Log.i(TAG, String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
		}
	}

	public void pushMessage(User user, String content) {
		/*
		 * @brief 推送单播消息(消息类型为透传，由开发方应用自己来解析消息内容) message_type = 0 (默认为0)
		 */

		// 1. 设置developer平台的ApiKey/SecretKey
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

		// 2. 创建BaiduChannelClient对象实例
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);

		// 3. 若要了解交互细节，请注册YunLogHandler类
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				Log.i(TAG, event.getMessage());
			}
		});

		try {

			// 4. 创建请求类对象
			// 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
			PushUnicastMessageRequest request = new PushUnicastMessageRequest();
			request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
										// 4:ios 5:wp
			request.setChannelId(user.getChannelId());
			request.setUserId(user.getUserId());

			request.setMessage(content);

			// 5. 调用pushMessage接口
			PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);

			// 6. 认证推送成功
			Log.i(TAG, "push amount : " + response.getSuccessAmount());

		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			e.printStackTrace();
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			Log.i(TAG, String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
		}
	}

	public void queryBindList(User user) {
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

		// 2. 创建BaiduChannelClient对象实例
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);

		// 3. 若要了解交互细节，请注册YunLogHandler类
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				// TODO Auto-generated method stub
				Log.i(TAG,event.getMessage());
			}
		});

		try {
			// 4. 创建请求类对象
			// 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
			QueryBindListRequest request = new QueryBindListRequest();
			request.setUserId(user.getUserId());

			// 5. 调用queryBindList接口
			QueryBindListResponse response = channelClient.queryBindList(request);

			// 6. 对返回的结果对象进行操作
			List<BindInfo> bindInfos = response.getBinds();
			for (BindInfo bindInfo : bindInfos) {
				long channelId = bindInfo.getChannelId();
				String userId = bindInfo.getUserId();
				int status = bindInfo.getBindStatus();
				Log.i(TAG,"channel_id:" + channelId + ", user_id: " + userId + ", status: " + status);

				String bindName = bindInfo.getBindName();
				long bindTime = bindInfo.getBindTime();
				String deviceId = bindInfo.getDeviceId();
				int deviceType = bindInfo.getDeviceType();
				long timestamp = bindInfo.getOnlineTimestamp();
				long expire = bindInfo.getOnlineExpires();

				Log.i(TAG,"bind_name:" + bindName + "\t" + "bind_time:" + bindTime);
				Log.i(TAG,"device_type:" + deviceType + "\tdeviceId" + deviceId);
				Log.i(TAG,String.format("timestamp: %d, expire: %d", timestamp, expire));
			}

		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			e.printStackTrace();
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			Log.i(TAG,String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
		}

	}

}
