
1.数据库配置在/extronline_online/src/main/webapp/WEB-INF/spring/root-context.xml

2.数据库为taxexam.sql

3.et_field 表修改  field_id 为 0  ，否则无法登陆。

4.考试系统管理员：admin  密码：xxb!(@zsx

5:服务器系统IP，账号，密码

无

6.考试系统yangyw账号
yangyw
123456

7.表et_user 的说明，et_org类似

status 表示是否已经答题
isall 是否有资格摇奖
iswin 是否得奖
winlevel 得奖等级
chance 是否必须中奖

8.
每次参与摇奖的人数要是开奖人数【抽3人】的5倍以上，否则滚动会出现滚动一遍，不在重复滚动问题。
各省能参与摇奖人数要大于等于2，
内置获奖人员不能一个省出现超过2个，同一个公司超过1个。

抽奖结果：每个省不超过2个人，每个公司不超过1个人.

9.抽奖关键表et_user,et_org  关键字段province【区分获奖省份】,company【et_user中区分公司】,name【et_org中区分公司】,isall,iswin,winlevel,chance
