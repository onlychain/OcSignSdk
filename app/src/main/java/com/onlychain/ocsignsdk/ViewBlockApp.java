package com.onlychain.ocsignsdk;



import com.onlychain.signsdk.wallet.base.ApiConfig;
import com.onlychain.signsdk.wallet.viewblock.GetBlockData;
import com.onlychain.signsdk.wallet.viewblock.HexConvertJson;

import java.util.List;

/**
 * 16进制Action本地解析，目前只保留actionType1和actionType4
 */
public class ViewBlockApp {
    public static void main(String[] args) {
        //更换节点IP
        ApiConfig.init("http://39.98.135.66:9082");

        //获取指定高度的区块数据并解析成json,目前只保留actionType1和actionType4
        final long height=1349624;
      new GetBlockData(String.valueOf(height)) {
               @Override
               public void getSuccess(StringBuffer json,List<String> tradList) {
                   System.out.println("通过API获取到指定高度的区块数据  ：  "+json);
                   for (String temp:tradList)
                   {
                     String result =  new HexConvertJson(temp).getJson();

                     if (result!=null)
                       System.out.println("得到高度: "+height+" 的 [tradingInfo] 数据，且"+"TYPE为"+
                               temp.substring(0,2)+"的数据 :  "
                               +result);
                   }
               }
           };


      //对指定的序列化Action数据进行解析,格式为json
        System.out.println();
        System.out.println("指定Action解析 ： "+new HexConvertJson("010101021aaea3ca7f1c55967907869884386ac47f291a2cb618f5052385a787bb4e7ddb00006a47304502206f77a998f3da616e246b1f7e8f9828a390da606d31825f0b906e773b77c47603022100da17bab74105c9333c240c4d5361d030664056b4a767b4b99e7d8be002c8982e210355c48844bb6392dbd251167c92904eabbbc1982b39dc7ebcb2141063f0281e1f1aaea3ca7f1c55967907869884386ac47f291a2cb618f5052385a787bb4e7ddb01006a47304502202b0a958bfd2fef35281273a1386ccca23112fe755bf4778ee73f9424d24f948c022100d7cee58f73d6c2968c7fdfa200392d7f2a240a6ef67298f6e3a4355d97fb5190210355c48844bb6392dbd251167c92904eabbbc1982b39dc7ebcb2141063f0281e1f02000000e8d4a51000001976a914274579901ace0417d662a203c8c3dbbb40693d8d88ac000064d9f81b8550001976a9143b349701a36b457339e53ead5159750efc1f2a8988ac00000000000000000000edbebafa05d4e95f0355c48844bb6392dbd251167c92904eabbbc1982b39dc7ebcb2141063f0281e1f01003045022006c695010408f24d99376fcaea26ca9e3e40a2a8bb0fd5880eb3867920d2db62022100f05337482d70aed28cd09e7c1182b173336f14212d086071ab7e269d67bf007d").getJson());
    }
}
