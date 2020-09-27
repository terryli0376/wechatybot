package me.yanand;

import cn.hutool.json.JSONObject;
import io.github.wechaty.Wechaty;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Room;
import io.github.wechaty.utils.QrcodeUtils;
import me.yanand.weather.WeatherUtil;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
        final String token = "";


        if (StringUtils.isBlank(token)) {
            throw new Exception("need a token");
        }
        Wechaty bot = Wechaty.instance(token);


        bot.onScan((qrcode, statusScanStatus, data) -> {
            System.out.println(QrcodeUtils.getQr(qrcode));
            System.out.println("Online Image: https://wechaty.github.io/qrcode/" + qrcode);
        });

        bot.onMessage(message -> {

            Contact from = message.from();
            Room room = message.room();

            String text = message.text();
            if(text.contains("@Terry") && text.contains("天气")){
                text = text.split(" ")[1].split("天气")[0];
                String msg = "";
                if(WeatherUtil.city.containsKey(text)){
                    JSONObject data = WeatherUtil.weather(text).getJSONObject("weatherinfo");
                    msg += data.getStr("cityname") + ":"+ data.getStr("weather") + " ";
                    msg += data.getStr("tempn")+"-"+data.getStr("temp");
                }else {
                    msg += "请输入正确地区！";
                }
                if (room != null) {
                    room.say(msg);
                } else {
                    from.say(msg);
                }
            }
        });

        bot.start(true);
    }
}
