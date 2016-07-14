package onepeak.com.appplug;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import onepeak.com.appplug.entity.PluginBean;

public class MainActivity extends ActivityGroup {

    private LocalActivityManager m_ActivityManager;
    private List<PluginBean> plugins;

    private LinearLayout llMainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_ActivityManager = getLocalActivityManager();

        llMainLayout = (LinearLayout)findViewById(R.id.linearLayout);

        attachPlugin(findPlugins());
    }

    /**
     * 加载插件列表
     * @param plugins
     */
    private void attachPlugin(final List<PluginBean> plugins){
        Log.e("ydt", "   ");
        Log.e("ydt", "----- 列出插件");
        Log.e("ydt", "----- "+plugins);
        this.plugins=plugins;
        for(final PluginBean plugin:plugins){
            final Button btn=new Button(this);
            btn.setTextColor(Color.RED);
            btn.setText(plugin.getLabel());

            llMainLayout.addView(btn);
            //添加事件
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    boolean isAttack=chbAttachMain.isChecked();
                    boolean isAttack=false;

                    Bundle bundle = new Bundle();
                    bundle.putString("a","AAAAAAAAAAA");
                    bundle.putString("b","BBBBBBBBBBB");
                    bundle.putString("c","CCCCCCCCCCC");

                    Intent it=new Intent();
                    it.putExtras(bundle);
                    it.setAction(plugin.getPakageName());

                    //是否附加为view
                    if(isAttack){
                        //这里偷下懒，这是演示插件作为view附加到主程序中的
                        for(PluginBean plugin:plugins){

                            Intent itt=new Intent();
                            itt.setAction(plugin.getPakageName());
                            ViewGroup view=(ViewGroup) (m_ActivityManager.startActivity("", itt)).getDecorView();
//                            wkMain.addView(view);

                        }
                        //一次性附加完毕算了，然后把按钮都删了，看着清净，这几个不是重点
                        llMainLayout.removeAllViews();
//                        chbAttachMain.setVisibility(View.GONE);
//                        wkMain.setToScreen(0);
                    }else{
                        //这里，不会把插件的窗体附加到主程序中，纯粹无用的演示
                        startActivity(it);
                    }
                }
            });


        }
    }

    /**
     * 查找插件
     * @return
     */
    private List<PluginBean> findPlugins(){

        List<PluginBean> plugins=new ArrayList<PluginBean>();


        //遍历包名，来获取插件
        PackageManager pm=getPackageManager();


        List<PackageInfo> pkgs=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for(PackageInfo pkg	:pkgs){
            //包名
            String packageName=pkg.packageName;
            String sharedUserId= pkg.sharedUserId;

            //sharedUserId是开发时约定好的，这样判断是否为自己人
            if(!"onepeak.test.app".equals(sharedUserId)||"onepeak.com.appplug".equals(packageName))
                continue;

            //进程名
            String prcessName=pkg.applicationInfo.processName;

            //label，也就是appName了
            String label=pm.getApplicationLabel(pkg.applicationInfo).toString();

            PluginBean plug=new PluginBean();
            plug.setLabel(label);
            plug.setPakageName(packageName);

            plugins.add(plug);
        }

        return plugins;

    }
}
