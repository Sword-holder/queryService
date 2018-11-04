# deepDiagnosis
这是一个关于故障诊断的多轮问答系统
# 运行环境
- python3
- jdk8
- Window或Linux
# 使用方式
### 1、将项目clone到本地，解压  
```
git clone git@github.com:Sword-holder/queryService.git
```
### 2、编译运行java搜索程序  
用Eclipse或者其它IDE打开项目所在文件夹，编译运行SocketServer.java程序  
### 3、打开一个terminal，运行python服务器程序  
进入项目文件夹目录  
执行python服务器代码  
```
python src/run.py
```
### 4、再开一个terminal，运行测试程序  
```
python src/server/test/test.py
```
### 5、输入问题，等待回答  
根据提示输入问题即可  
因为如果要保证准确性，文档需要经过人工审查与处理（因为文档本身很不规整），目前仅手动处理了**前26个问题**，作为Demo。虽然后面采用程序批处理的方式也做了处理，但不敢保证准确度以下是25个问题列表：  
- 如何重建消息跟踪数据库（MTSTORE.NSF）和MTDATA子目录？
- 升级Domino到7.0.3版本时，会安装多个Windows服务 
- 在 Domino 5.x 中用简单操作更新读者域或作者域会引起问题 
- 如何修复vpuserinfo.nsf数据库？
- IBM Notes 9.0标准配置宕机了。IBM 技术支持需要收集哪些文件？
- Sametime 7.5.1：用户无法参加会议 
- 在Domino6的Notes.ini文件中与事务日志有关的参数 
- IBM Lotus Notes/Domino技术知识文档 
- 当用户试图将假日信息导入到IBM ® Lotus Notes / Domino®中邮件文件的日历中时，Notes客户端报错："数据库中已存在一个ID相同文档（Notes Error: Database already contains a document with this ID）" 。
- 您在Lotus® Domino® 6.0上配好SSL后，然后在Notes日志中发现充斥着SSL握手失败信息，错误代码都是4165。完整的错误信息如下：
"HTTP Server: SSL handshake failure, IP address [10.0.0.1], Keyring 
[selfcert.kyr], [SSL Error: Network IO error], code [4165]" 
- 如何在iNotes上启用邮件库的全文索引 
- Notes 8.5 - 必需的 Linux 软件包和更新 
- 如何通过公式显示当前视图中选中文档的数量 
- 该文档包含了2010年4月期的技术支持文档邮件(Lotus FAQ)内容 
- 用微软终端服务或微软远程桌面启动Sametime服务器的时候，用户发现会议服务和一些社区服务（典型的有STDirectory或STUsers）没有加载成功。这使得用户无法登录到Sametime客户端，会议也无法正常启动。 
- 选择安装成Windows服务之后无法看到Domino服务器控制台你把Lotus® Domino®安装成了Microsoft® Windows® 2003系统上的服务。虽然你选择了“允许应用程序与桌面交互”，Domino服务器控制台仍然不可见 
- 在 iNotes 签名中可以使用 HTML 添加图像吗？用户找到如何定制签名，但却是纯文本的。有没有办法在这个文本域里包含图像、照片或 HTML 标记呢？
- Notes 6.x 用户改名后，打开邮件文件很慢在Notes/Domino 6.x系统中，用AdminP管理进程给用户改名后，当用户使用自己的标识符文件打开邮件数据库的时候，会有一个显著的延迟。有些用户甚至报告说延迟会超过10分钟以上。下面两种情况没有延迟现象：1)用户的邮件数据库不包含任何文件夹；2）打开邮件数据库时不是用更名用户的标识符文件。
为什么？ 
- Domino/Notes 在新建复本和复制之前会不会检查是否有足够的磁盘空间？
- 启动 HTTP 任务时报错：“Error loading Web SSO Configuration document”Error loading Web SSO Configuration document您启用 Domino 服务器做多服务器会话验证，也称作单点用户登录 (SSO), 并且创建了 Web SSO 配置文档。当您启动 HTTP 任务的时候，您却注意到下面的错误："HTTP Server: Error loading Web SSO Configuration 'LtpaToken' (Single Sign-On 
configuration is invalid)" 
- 使用SMTP_Config_Update_Interval参数来观察SMTP的配置是否被更新
Domino SMTP服务器多长时间会检查SMTP配置信息是否被更新呢？如何实现的呢？
- http服务器验证用户的时候引发宕机 
- 是否能阻止“离开办公室”（Out Of Office）回复垃圾邮件？
- 在Windows Mobile设备上使用Traveler时如何延长电池的使用寿命 
- 使用Lotus Notes Traveler时有什么数据可以存放在内存卡/存储卡中？
- 迁移后被归档的文档出现在“所有文档”文件夹中当您将一组用户从 Domino R5 迁移到 Domino 6.5.1后，您发现他们的已经被归档的文档出现在“所有文档”文件夹中，而不是在其指定的文件夹中。直接在 Domino 6 版本中注册的用户 (相对于被迁移的用户)则没有这个问题。
# 运行效果截图
# 工作原理
系统分为前端和后端，前端负责接口呈现、与用户进行交互的部分，后端负责搜索答案的部分。采用分离的原因一方面是可以使系统功能模块更加明确，另一方面是可以做一个分布式系统  
前端采用python的flask框架，它用于接受请求，并交给后端处理。  
后端使用java编写，搜索答案的过程类似于搜索引擎的原理，在预处理时，先对文档进行分词，再生成索引表，当询问到达时，用同样的方式进行分词，再对这些词进行散列，并形成打分的结果列表。。然后我自定义了一套筛选规则，将仅仅是擦到一点边的答案给过滤掉，并优化了询问过程。这一系列操作使用的时Apache的Lucene开源库。  
前后端之间的通讯采用socket，也就是基于传输层及以下的协议进行通信，另外我还自己规定了一些规则，如后端"disc"结尾的意味着主动断开连接，"next"表示进行下一次询问。  
前端采用RESTful风格的接口定义，详细请参见[接口文档]。  
另外，我还自己写了一个测试程序，运行该程序可直接连接客服，用于方便地测试程序效果，程序放在./src/server/test/test.py  
处理完的文档文件放在./info中，下面的每一个文件夹对应的是一个字段的信息  
# 开发过程
首先在预处理过程中，将文本中的有关于该问题的信息提取，包括问题描述、产品、版本、平台、解决方案，这部分我尝试了用程序去批处理，但因为文档结构太不规整，效果不够理想。因此在测试时，仅使用了人工处理的25个问题作为样例。  
接着是对这些文档进行扫描，生成倒排的索引表。  
搜索答案时会将索引表读入内存，然后直接散列到答案，速度非常快，不存在性能上的问题。  
但这种方式存在缺陷，就是说搜索的词偏向于词的堆叠，词与词之间的关系无法从搜索中体现出来。  
因此我又尝试了引入知识图谱的方式，但在用户询问的自然语言处理中遇到了严重瓶颈，因此放弃。  