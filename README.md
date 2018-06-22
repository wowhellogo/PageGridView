# VpGridView
ViewPager+GridView组合控件实现ViewPager+GridView实现网格布局分页效果

## PageGridView自定义属性：
属性名 | 说明 | 默认值
:----------- | :----------- | :-----------
pageSize                | 每页大小               | 8
numColumns              | 列数                   | 4
isShowIndicator         | 是否显示指示器          | true
selectedIndicator       | 选中指示点资源ID        | R.mipmap.ic_dot_selected
unSelectedIndicator     | 未选中指示点资源ID      | R.mipmap.ic_dot_normal

## 效果图
<img src="./img/image1.jpg"  height="800" width="480">


## PageGridView使用
### 布局

```xml


  <com.pagegridviewlibrary.PageGridView
          android:id="@+id/vp_grid_view"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          app:numColumns="4"
          app:pageSize="4"
          app:selectedIndicator="@mipmap/ic_dot_selected"
          app:unSelectedIndicator="@mipmap/ic_dot_normal"
          app:isShowIndicator="true"
          />


```
### Model
继承VpGridView.ItemModel 为item赋值和设置图标

```java

public class MyIconModel extends PageGridView.ItemModel {
    private String name;


    private int iconId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public MyIconModel(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    @Override
    protected String getItemName() {
        return name;
    }

    @Override
    protected void setIcon(ImageView imageView) {
        imageView.setImageResource(iconId);
    }
}

```

### setData

```java

public class MainActivity extends AppCompatActivity {

    List<MyIconModel> mList;

    private PageGridView<MyIconModel> mPageGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPageGridView =findViewById(R.id.vp_grid_view);
        initData();
        mPageGridView.setData(mList);
        mPageGridView.setOnItemClickListener(new PageGridView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initData() {
        mList=new ArrayList<>();
        for(int i=0;i<5;i++){
            mList.add(new MyIconModel("测试"+i,R.mipmap.ic_launcher));
        }
    }
}

```


