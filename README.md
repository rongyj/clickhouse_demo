# 1. ClickHouse简介

ClickHouse是一个用于联机分析(OLAP)的列式数据库管理系统(DBMS)。

OLAP场景的关键特征 
* 绝大多数是读请求
* 数据以相当大的批次(> 1000行)更新，而不是单行更新;或者根本没有更新。
* 已添加到数据库的数据不能修改。
* 对于读取，从数据库中提取相当多的行，但只提取列的一小部分。
* 宽表，即每个表包含着大量的列
* 查询相对较少(通常每台服务器每秒查询数百次或更少)
* 对于简单查询，允许延迟大约50毫秒
* 列中的数据相对较小：数字和短字符串(例如，每个URL 60个字节)
* 处理单个查询时需要高吞吐量(每台服务器每秒可达数十亿行)
* 事务不是必须的
* 对数据一致性要求低
* 每个查询有一个大表。除了他以外，其他的都很小。
* 查询结果明显小于源数据。换句话说，数据经过过滤或聚合，因此结果适合于单个服务器的RAM中
* 很容易可以看出，OLAP场景与其他通常业务场景(例如,OLTP或K/V)有很大的不同， 因此想要使用OLTP或Key-Value数据库去高效的处理分析查询场景，并不是非常完美的适用方案。例如，使用OLAP数据库去处理分析请求通常要优于使用MongoDB或Redis去处理分析请求。 

列存储数据库更适合OLAP，原因分为两方面

**输入/输出**

* 针对分析类查询，通常只需要读取表的一小部分列。在列式数据库中你可以只读取你需要的数据。例如，如果只需要读取100列中的5列，这将帮助你最少减少20倍的I/O消耗。
* 由于数据总是打包成批量读取的，所以压缩是非常容易的。同时数据按列分别存储这也更容易压缩。这进一步降低了I/O的体积。
* 由于I/O的降低，这将帮助更多的数据被系统缓存。

**CPU**

由于执行一个查询需要处理大量的行，因此在整个向量上执行所有操作将比在每一行上执行所有操作更加高效。同时这将有助于实现一个几乎没有调用成本的查询引擎。如果你不这样做，使用任何一个机械硬盘，查询引擎都不可避免的停止CPU进行等待。所以，在数据按列存储并且按列执行是很有意义的。

有两种方法可以做到这一点：

  * 向量引擎：所有的操作都是为向量而不是为单个值编写的。这意味着多个操作之间的不再需要频繁的调用，并且调用的成本基本可以忽略不计。操作代码包含一个优化的内部循环。

  * 代码生成：生成一段代码，包含查询中的所有操作。

这是不应该在一个通用数据库中实现的，因为这在运行简单查询时是没有意义的。但是也有例外，例如，MemSQL使用代码生成来减少处理SQL查询的延迟(只是为了比较，分析型数据库通常需要优化的是吞吐而不是延迟)。

请注意，为了提高CPU效率，查询语言必须是声明型的(SQL或MDX)， 或者至少一个向量(J，K)。 查询应该只包含隐式循环，允许进行优化。

# 2. 安装部署

## 2.1. 系统要求

ClickHouse可以在任何具有x86_64，AArch64或PowerPC64LE CPU架构的Linux，FreeBSD或Mac OS X上运行。

官方预构建的二进制文件通常针对x86_64进行编译，并利用SSE 4.2指令集，因此，除非另有说明，支持它的CPU使用将成为额外的系统需求。下面是检查当前CPU是否支持SSE 4.2的命令:

```powershell
grep -q sse4_2 /proc/cpuinfo && echo "SSE 4.2 supported" || echo "SSE 4.2 not supported"
```

![20201210110459](https://liulv.work/images/img/20201210110459.png)

要在不支持SSE 4.2或AArch64，PowerPC64LE架构的处理器上运行ClickHouse，您应该通过适当的配置调整从源代码构建ClickHouse。

## 2.2. 下载安装

官方支持DEB、RPM、Tgz、Docker安装，这里使用RPM的包

推荐使用CentOS、RedHat和所有其他基于rpm的Linux发行版的官方预编译rpm包。

首先，您需要添加官方存储库：

```powershell
sudo yum install yum-utils
sudo rpm --import https://repo.clickhouse.tech/CLICKHOUSE-KEY.GPG
sudo yum-config-manager --add-repo https://repo.clickhouse.tech/rpm/stable/x86_64
```

如果您想使用最新的版本，请用testing替代stable(我们只推荐您用于测试环境)。prestable有时也可用。

然后运行命令安装：

```powershell
sudo yum install clickhouse-server clickhouse-client
```

你也可以从这里手动下载安装包：[下载](https://repo.clickhouse.tech/rpm/stable/x86_64)。

## 2.3. 启动

```powershell
sudo /etc/init.d/clickhouse-server start
```

或

```powershell
sudo /etc/init.d/clickhouse-server start
```

日志文件将输出在`/var/log/clickhouse-server/`文件夹。

如果服务器没有启动，检查`/etc/clickhouse-server/config.xml`中的配置。

您也可以手动从控制台启动服务器:

```powershell
sudo -u clickhouse-server --config-file=/etc/clickhouse-server/config.xml
```

在这种情况下，日志将被打印到控制台，这在开发过程中很方便。

如果配置文件在当前目录中，则不需要指定——config-file参数。默认情况下，它的路径为`./config.xml`。

ClickHouse支持访问限制设置。它们位于users.xml文件(与config.xml同级目录)。
默认情况下，允许default用户从任何地方访问，不需要密码。可查看`user/default/networks`。

更多信息，请参见[Configuration Files](https://clickhouse.tech/docs/zh/operations/configuration-files/)。

## 2.4. 设置运行远程访问

打开/etc/clickhouse-server/config.xml

把注释掉的<listen_host>::</listen_host>取消注释，然后重启服务

sudo systemctl restart clickhouse-server

检查端口

    lsof -i :8123

```log
[root@localhost ~]# lsof -i :8123
COMMAND     PID       USER   FD   TYPE DEVICE SIZE/OFF NODE NAME
clickhous 16848 clickhouse   55u  IPv6  79734      0t0  TCP *:8123 (LISTEN)
[root@localhost ~]# 
```

# 3. 连接

## 通过命令客户端连接

启动服务后，您可以使用命令行客户端连接到它

```powershell
clickhouse-client
```

![20201210113200](https://liulv.work/images/img/20201210113200.png)

## 通过DBServer客户端连接

下载DBServer客户端，然后下载ClickHouse驱动并配置连接

![20201210113622](https://liulv.work/images/img/20201210113622.png)


# 建工程测试表

通过上面的客户端连接成功后，通过工程中的user-info.sql创建表并插入测试数据后，就可以运行工程开发测试。












