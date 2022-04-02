# Solid

## 使用说明

#### 1.添加依赖
在project的build.gradle里面添加工程依赖


allprojects {

		repositories {
		
			maven { url 'https://jitpack.io' }
			
		}
		
}
  
  
#### 2.在模块的build.gradle里面添加模块依赖


dependencies {

	  implementation 'com.github.1249848166:Solid:1.0.0'
		
}
  
  
#### 3.项目中的使用

##### view端

public class MainActivity extends AppCompatActivity implements SolidBaseView {

    private RecyclerView recycler;
    private ListAdapter adapter;
    private int clickPosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler=findViewById(R.id.recycler);

        //调用注册，完成View和Data的绑定
        Solid.getInstance().register(solidId(),this,new MainActivityData());
        //刚进入界面，调用显示列表事件,类型是数据从data端传向view端
        Solid.getInstance().call(Config.BIND_ID_LIST_VIEW, Solid.CallType.CALL_TYPE_DATA_TO_VIEW);
    }

    /**
     * 这里是view的接收端，从data那里获得数据，获取的数据可以是空的
     * @param data 数据
     * @param msg 消息
     */
    @SuppressWarnings("unchecked")
    @SolidView(bindId = Config.BIND_ID_LIST_VIEW)
    void showList(Object data,String msg){
        if(data==null){
            System.out.println("这里获得的数据是null，考虑我没有在数据端传入，而是想要通过provider获取");
            final List<String> testItems= (List<String>) Solid.getInstance()
                    .queryProviderData(solidId(),Config.PROVIDER_ID_LIST_VIEW_DATA);
            show(testItems);
        }else{
            System.out.println("我传入了数据，拿到了");
            final List<String> testItems= (List<String>) data;
            show(testItems);
        }
    }

    private void show(List<String> testItems) {
        adapter=new ListAdapter(testItems);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                RecyclerView.VERTICAL,false));
        adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                //点击后打开一个对话框，询问是否删除item，作为view向data传递消息的测试
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("是否删除item")
                        .setCancelable(false)
                        .setNegativeButton("no", (dialogInterface, i)
                                -> dialogInterface.dismiss())
                        .setPositiveButton("yes", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            clickPosition=position;
                            //点击了确认之后，就向data传递数据，并指定是从view传向data的类型
                            Solid.getInstance().call(Config.BIND_ID_ITEM_REMOVE,
                                    Solid.CallType.CALL_TYPE_VIEW_TO_DATA);
                        })
                        .show();
            }
        });
    }

    /**
     * 移除item对应的方法，事件发送端
     * @param callback
     */
    @SolidView(bindId = Config.BIND_ID_ITEM_REMOVE)
    void listItemRemove(SolidCallback callback){
        callback.onDataGet(clickPosition);
    }

    /**
     * 这里是刷新列表事件的接收端
     * @param data
     * @param msg
     */
    @SolidView(bindId = Config.BIND_ID_LIST_REFRESH)
    void noticeRefreshList(Object data,String msg){
        runOnUiThread(()->{
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Solid.getInstance().unRegister(solidId());//进行注销，回收资源，避免内存泄漏
    }

    @Override
    public int solidId() {
        return Config.SOLID_ID_MAIN_ACTIVITY;
    }
}


##### data端

public class MainActivityData implements SolidBaseData {

    private final List<String> testItems =new ArrayList<>();//数据类里面管理并持有对应页面的所有数据

    /**
     * 列表显示事件发送端
     * 获取数据的方法，需要自定义一个bindId进行和view的绑定，
     * 并且因为是数据提供者，所以需要指定SolidCallback,通过callback传入数据
     * @param callback 用来传入数据给Solid
     */
    @SolidData(bindId = Config.BIND_ID_LIST_VIEW)
    void getListViewData(SolidCallback callback){
        //模拟从服务器获取数据的异步操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                //随便给一些数据进行测试
                testItems.add("因为第一次使用MVP架构，觉得痛苦，所以写了这个框架");
                testItems.add("希望有朝一日他能被很多人运用，并且取代MVP");
                testItems.add("这个项目一开始一定有很多地方不太优化，也比较单一");
                testItems.add("希望大家多多提出需求或者意见，让我不断改进");
                //callback.onDataGet(testList);
                callback.onDataGet(null);//这里可以传入null，然后通过provider获取也可以
                //callback.onMessageGet("数据拿到了");
            }
        }).start();
    }

    /**
     * item移除事件接收端
     * view那边点击删除之后，这边会响应
     * @param data 因为是删除，所以数据是要删除的item位置
     * @param msg 不需要
     */
    @SolidData(bindId = Config.BIND_ID_ITEM_REMOVE)
    void listItemDataRemove(Object data,String msg){
        testItems.remove((int)data);
        //删除之后，提醒view更新显示
        Solid.getInstance().call(Config.BIND_ID_LIST_REFRESH,
                Solid.CallType.CALL_TYPE_DATA_TO_VIEW);
    }

    /**
     * 列表刷新事件发送端
     * @param callback
     */
    @SolidData(bindId = Config.BIND_ID_LIST_REFRESH)
    void noticeListToRefresh(SolidCallback callback){
        callback.onDataGet(null);
    }

    /**
     * 列表数据提供者
     * 理论上，只要bindView绑定的页面和数据没有回收，那么就可以在任意地方获取到
     * 建议使用query的方式搜索数据，而非接口传入
     * @return 你想要返回给view的数据
     */
    @SolidDataProvider(id=Config.PROVIDER_ID_LIST_VIEW_DATA)
    List<String> testListData(){
        return testItems;
    }

    /**
     * solidId用来标注数据类与试图类，比如这个项目，
     * MainActivity和MainActivityData用同一个solidId标注
     * 一个页面启用一个solidId
     * @return 返回一个solidId给Solid
     */
    @Override
    public int solidId() {
        return Config.SOLID_ID_MAIN_ACTIVITY;//我们可以用一个配置类管理所有id
    }
}

#### 4.目前所有API

##### 1.事件注册与调用

//事件发送端

@SolidData(bindId = Config.BIND_ID_LIST_VIEW)

void getListViewData(SolidCallback callback){

		callback.onDataGet(data);
		
}

//事件接收端

@SolidView(bindId = Config.BIND_ID_LIST_VIEW)

void showList(Object data,String msg){


}

//调用事件的方法

Solid.getInstance().call(Config.BIND_ID_LIST_VIEW, Solid.CallType.CALL_TYPE_DATA_TO_VIEW);

##### 2.任意位置获取provider提供的数据

//定义provider

@SolidDataProvider(id=Config.PROVIDER_ID_LIST_VIEW_DATA)

List<String> testListData(){
	
		return testItems;
	
}

//调用方法获取数据
	
Solid.getInstance().queryProviderData(solidId(),Config.PROVIDER_ID_LIST_VIEW_DATA);
	
#### 5.更多使用，还在开发中

如果你有什么疑问或者建议，欢迎联系qq：1249848166
