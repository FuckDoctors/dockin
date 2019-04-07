package com.eap.wechat.controller;

import com.eap.framework.utils.PropertiesUtil;
import com.eap.framework.wxtools.api.WxConfig;
import com.eap.framework.wxtools.api.WxConsts;
import com.eap.framework.wxtools.api.WxMessageRouter;
import com.eap.framework.wxtools.bean.WxMenu;
import com.eap.framework.wxtools.bean.WxUserList;
import com.eap.framework.wxtools.bean.WxXmlMessage;
import com.eap.framework.wxtools.bean.WxXmlOutMessage;
import com.eap.framework.wxtools.bean.result.WxOAuth2AccessTokenResult;
import com.eap.framework.wxtools.exception.WxErrorException;
import com.eap.framework.wxtools.handler.DemoHandler;
import com.eap.framework.wxtools.interceptor.DemoInterceptor;
import com.eap.framework.wxtools.matcher.DemoMatcher;
import com.eap.framework.wxtools.service.WxService;
import com.eap.framework.wxtools.util.xml.XStreamTransformer;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by billJiang on 2017/7/21.
 * e-mail:475572229@qq.com  qq:475572229
 * 微信公众号
 */
@Controller
@RequestMapping(value="/wechat")
public class AssessController {

    @Resource
    private WxService wxService;

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public void signature(
            @RequestParam(value = "signature", required = true) String signature,
            @RequestParam(value = "timestamp", required = true) String timestamp,
            @RequestParam(value = "nonce", required = true) String nonce,
            @RequestParam(value = "echostr", required = true) String echostr,
            HttpServletResponse response) throws IOException {
        String[] values = {PropertiesUtil.getValue("wx.token"), timestamp, nonce};
        Arrays.sort(values); // 字典序排序
        String value = values[0] + values[1] + values[2];
        String sign = DigestUtils.shaHex(value);
        PrintWriter writer = response.getWriter();
        if (signature.equals(sign)) {// 验证成功返回ehcostr
            writer.print(echostr);
        } else {
            writer.print("error");
        }
        writer.flush();
        writer.close();


       /* PrintWriter out = response.getWriter();
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (iService.checkSignature(signature, timestamp, nonce, echostr)) {
            out.print(echostr);
        }*/
    }


    @RequestMapping(value="/callback",method = RequestMethod.POST)
    public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        // 返回消息给微信服务器
        PrintWriter out = response.getWriter();
        // 获取encrypt_type 消息加解密方式标识
        String encrypt_type = request.getParameter("encrypt_type");
        // 创建一个路由器
        WxMessageRouter router = new WxMessageRouter(wxService);

        WxXmlMessage wx = XStreamTransformer.fromXml(WxXmlMessage.class, request.getInputStream());
        WxMenu menu = new WxMenu();
        List<WxMenu.WxMenuButton> btnList = new ArrayList<>();

        //设置CLICK类型的按钮1
        WxMenu.WxMenuButton btn1 = new WxMenu.WxMenuButton();
        btn1.setType(WxConsts.BUTTON_VIEW);
        btn1.setUrl("https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzAwODU4NDQ5OA==#wechat_redirect");
        btn1.setName("历史文章");

        //设置VIEW类型的按钮2
        WxMenu.WxMenuButton btn2 = new WxMenu.WxMenuButton();
        btn2.setType(WxConsts.BUTTON_VIEW);
        btn2.setUrl("http://code.admineap.com/blog/bc4d163c5d45ac86015d55d2bd430003");
        btn2.setName("AdminEAP");

        //设置含有子按钮的按钮3
        WxMenu.WxMenuButton btn3 = new WxMenu.WxMenuButton();
        btn2.setType(WxConsts.BUTTON_CLICK);
        btn2.setKey("translate");
        btn2.setName("名词解析");

        //将三个按钮设置进btnList
        btnList.add(btn1);
        btnList.add(btn2);
        btnList.add(btn3);
        //设置进菜单类
        menu.setButton(btnList);
        //调用API即可
        try {
            //参数1--menu  ，参数2--是否是个性化定制。如果是个性化菜单栏，需要设置MenuRule
            wxService.createMenu(menu, false);
        } catch (WxErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            // 判断消息加解密方式，如果是加密模式。encrypt_type==aes
            if (encrypt_type != null && "aes".equals(encrypt_type)) {
//				String signature = request.getParameter("signature");
                String timestamp = request.getParameter("timestamp");
                String nonce = request.getParameter("nonce");
                String msg_signature = request.getParameter("msg_signature");

                // 微信服务器推送过来的加密消息是XML格式。使用WxXmlMessage中的decryptMsg()解密得到明文。
                 wx = WxXmlMessage.decryptMsg(request.getInputStream(), WxConfig.getInstance(), timestamp,
                        nonce, msg_signature);
                System.out.println("消息：\n " + wx.toString());
                // 添加规则；这里的规则是所有消息都交给DemoMatcher处理，交给DemoInterceptor处理，交给DemoHandler处理
                // 注意！！每一个规则，必须由end()或者next()结束。不然不会生效。
                // end()是指消息进入该规则后不再进入其他规则。 而next()是指消息进入了一个规则后，如果满足其他规则也能进入，处理。
                router.rule().matcher(new DemoMatcher()).interceptor(new DemoInterceptor()).handler(new DemoHandler()).end();
                // 把消息传递给路由器进行处理，得到最后一个handler处理的结果
                WxXmlOutMessage xmlOutMsg = router.route(wx);
                if (xmlOutMsg != null) {
                    // 将要返回的消息加密，返回
                    out.print(WxXmlOutMessage.encryptMsg(WxConfig.getInstance(), xmlOutMsg.toXml(), timestamp, nonce));// 返回给用户。
                }
                //如果是明文模式，执行以下语句
            } else {
                // 微信服务器推送过来的是XML格式。
                //WxXmlMessage wx = XStreamTransformer.fromXml(WxXmlMessage.class, request.getInputStream());
                System.out.println("消息：\n " + wx.toString());
                // 添加规则；这里的规则是所有消息都交给DemoMatcher处理，交给DemoInterceptor处理，交给DemoHandler处理
                // 注意！！每一个规则，必须由end()或者next()结束。不然不会生效。
                // end()是指消息进入该规则后不再进入其他规则。 而next()是指消息进入了一个规则后，如果满足其他规则也能进入，处理。
                router.rule().matcher(new DemoMatcher()).interceptor(new DemoInterceptor()).handler(new DemoHandler()).end();
                // 把消息传递给路由器进行处理
                WxXmlOutMessage xmlOutMsg = router.route(wx);
                if (xmlOutMsg != null)
                    out.print(xmlOutMsg.toXml());// 因为是明文，所以不用加密，直接返回给用户。
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    @RequestMapping(value="/oauth",method = RequestMethod.GET)
    public void oauth(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        System.out.println("code:"+code+" state:"+state);
        PrintWriter out = response.getWriter();
        WxOAuth2AccessTokenResult result = null ;
        try {
            result = wxService.oauth2ToGetAccessToken(code);
            WxUserList.WxUser user = wxService.oauth2ToGetUserInfo(result.getAccess_token(), new WxUserList.WxUser.WxUserGet(result.getOpenid(), WxConsts.LANG_CHINA));
            System.out.println(user.toString());
            out.print(user.toString());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }


}
