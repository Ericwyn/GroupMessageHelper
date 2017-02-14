# GroupMessageHelper
为校团委写的一款短信群发软件，使用了jxl读取Excel文件，自动群发，省去了社团招新时候各个人慢慢发送短信的麻烦

##实现
- 使用了jxl来对excel文件进行读取和分析

##功能
- 智能的对包含（姓名-联系电话）两列的Excel文进行分析，提取成通讯录
- 单独选择通讯录中的人员进行短信的群发

##注意
- 支持安卓版本最低为Android5.1
- 仅支持Excel 2003及之前的Excel文件（即后缀名为.xls的文件）

##发布
###v1.0 [点击下载](https://github.com/Ericwyn/GroupMessageHelper/releases/download/v1.0/app-release-v1.0.apk)
- 实现了全部的基本功能
- 在Android5.1（MX5 with bugme6.0）、Android6.0（Moto x2）、Android7.0（Android Emulator）上面测试通过