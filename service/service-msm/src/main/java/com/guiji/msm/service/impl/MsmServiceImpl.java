package com.guiji.msm.service.impl;

import com.alibaba.fastjson.JSON;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.guiji.msm.service.MsmService;
import com.guiji.vo.msm.MsmVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(String phone, String code) {
        //生产环境请求地址：app.cloopen.com
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = "8a216da882f1f5940182fd85eeed0291";
        String accountToken = "305ebb0de48b48e78759abfcff87659f";
        //请使用管理控制台中已创建应用的APPID
        String appId = "8a216da882f1f5940182fd85f00b0298";
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        String templateId = "1";
        String[] datas = {code, "1"};
//        String subAppend="1234";  //可选 扩展码，四位数字 0~9999
//        String reqId="fadfafas";  //可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
        HashMap<String, Object> result = sdk.sendTemplateSMS(phone, templateId, datas);
//        HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas,subAppend,reqId);
        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
            return true;
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
        }
        return false;
    }

//    mq发送短信封装
//     @Override
//     public boolean send(MsmVo msmVo) {
//         if(!StringUtils.isEmpty(msmVo.getPhone())) {
//             return this.send(msmVo.getPhone(), msmVo.getParam());
//         }
//         return false;
//     }

//     public boolean send(String phone, Map<String,Object> param){
//         //判断手机号是否为空
//         if(StringUtils.isEmpty(phone)) {
//             return false;
//         }
//         //生产环境请求地址：app.cloopen.com
//         String serverIp = "app.cloopen.com";
//         //请求端口
//         String serverPort = "8883";
//         //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
//         String accountSId = "8a216da882f1f5940182fd85eeed0291";
//         String accountToken = "305ebb0de48b48e78759abfcff87659f";
//         //请使用管理控制台中已创建应用的APPID
//         String appId = "8a216da882f1f5940182fd85f00b0298";
//         CCPRestSmsSDK sdk = new CCPRestSmsSDK();
//         sdk.init(serverIp, serverPort);
//         sdk.setAccount(accountSId, accountToken);
//         sdk.setAppId(appId);
//         sdk.setBodyType(BodyType.Type_JSON);
//         String templateId = "1";
//         String params = JSON.toJSONString(param);
//         String[] datas = {params, "1"};
//         HashMap<String, Object> result = sdk.sendTemplateSMS(phone, templateId, datas);
//         if ("000000".equals(result.get("statusCode"))) {
//             //正常返回输出data包体信息（map）
//             HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
//             Set<String> keySet = data.keySet();
//             for (String key : keySet) {
//                 Object object = data.get(key);
//                 System.out.println(key + " = " + object);
//             }
//             return true;
//         } else {
//             //异常返回输出错误码和错误信息
//             System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
//         }
//         return false;
//     }

}
