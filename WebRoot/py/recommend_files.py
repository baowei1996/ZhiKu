import os
import sys
import pymysql
import get_all_user_tag

# 6.11 欠缺鲍伟提供的输出口
# 6.14 添加输出口

'''
"files": [
            {
                "fid": 34, 
                    "nickname": "111", 
                "fileinfo": {
                    "name": "高级程序设计语言_山软智库.pdf", 
                    "course": "高级程序设计语言java", 
                    "dncnt": 0, 
                    "uptime": "2018-06-10 19:09", 
                    "desc": "智库所编写的原创文档"
                }
            }
        ]
'''

# 打开数据库连接
db = pymysql.connect("719daze.me", "jpidea", "jp", "zhiku", charset='utf8')
# 使用cursor()方法获取操作游标
cursor = db.cursor()

#获取文件内容
def getData(filename):
    fr = open(filename,encoding="utf-8")
    data = []
    for line in fr:
        line = line.replace("\n", "")
        data.append(line.split(","))
    fr.close()
    return data

#把标签list转化合并为string，并加在每篇文章的最后一列
def list2String():
    for i in range(len(data)):
        string = ""
        for j in range(1,len(data)-1):
            string=string+data[i][j]
        data[i].append(string)

    # print (data)

#python 两个list 求交集，并集，差集 - CSDN博客  https://blog.csdn.net/bitcarmanlee/article/details/51622263
#获取两个list的交集 返回值是list
def intersection(listA,listB):
    #求交集的两种方式
    retA = [i for i in listA if i in listB]
    # retB = list(set(listA).intersection(set(listB)))
    return retA
#获取两个list的并集 返回值是list
def union(listA,listB):
    #求并集
    retC = list(set(listA).union(set(listB)))
    return retC
#获取两个list的补集 返回值是list
def complement(listA,listB):
    #求差集，在B中但不在A中
    retD = list(set(listB).difference(set(listA)))
    return retD

#获取权重最小的序号index
def getMinIndex(list):
    min=100
    if(len(list)==1):
        index=0
    else:
        for i in range(len(list)):
            if (list[i] < min):
                min = list[i]
                index = i
    return index

#寻找与其最相似的文章
def findSimilarity(data,singleDataAsList):
    # 生成包含匹配数目最多的文章的列表。如[[7, '进程', '内存'], [10, '进程', '内存']]
    allresult = [["初始值"]]
    max = -1
    for i in range(len(data)):
        if (data[i]!=singleDataAsList): #不能与自身比较
            a = data[i]
            intersectionResult = intersection(a,singleDataAsList)
            # print (intersectionResult)
            if (len(intersectionResult) > max and len(intersectionResult) != 0):  # and len(intersectionResult)!=0
                max = len(intersectionResult)
                allresult.pop()
                intersectionResult.insert(0, i)
                allresult.append(intersectionResult)
            elif (len(intersectionResult) == max):
                intersectionResult.insert(0, i)
                allresult.append(intersectionResult)

    # 生成相应的列表的权重 如[5, 10]
    allweight = [0] * len(allresult)
    for i in range(len(allresult)):
        for j in range(1, len(allresult[i])):
            for k in range(len(data[allresult[i][0]])):
                if (data[allresult[i][0]][k] == allresult[i][j]):
                    allweight[i] = allweight[i] + k

    # print (allresult)
    # print (allweight)

    minIndex = getMinIndex(allweight)
    uid = data[allresult[minIndex][0]][0]

    return  uid

#通过uid获取一个用户所下载或上传过的所有文件，fids，返回值是一个list。  fids
def getFidsByUid(uid):

    # SQL 查询语句
    sql = "SELECT * FROM zhiku.file_op   where (type=1 or type=0 ) and uid="+str(uid) +" order by optime desc"
    # print (sql)
    # 执行SQL语句
    cursor.execute(sql)
    # 获取所有记录列表
    results = cursor.fetchall()
    fids = []
    for row in results:
        fid = row[0]
        uid = row[1]
        optime = row[2]
        type = row[4]
        # 打印结果
        if (fid not in fids):
            fids.append(fid)
        # print (fid, uid, optime, type)

    # print (fids)
    return fids


def getRecommendFilesID(uid1,uid2):
    fids1=getFidsByUid(uid1)
    fids2=getFidsByUid(uid2)
    # print (fids1)
    # print (fids2)
    # ll=intersection(fids1,fids2)
    recommendFids=complement(fids1,fids2) #uid2下载过但uid1未下载过的文件集合

    # print (recommendFids)
    return recommendFids

def getRecommendFiles(recommendFids):
    RecommendFiles = []
    for i in range(len(recommendFids)):
        # SQL 查询语句
        sql = "SELECT * FROM zhiku.file_info   where fid=" + str(recommendFids[i])
        # print (sql)
        # 执行SQL语句
        cursor.execute(sql)
        # 获取所有记录列表
        results = cursor.fetchall()
        for row in results:
            name = row[2]
            # 打印结果
            if (name not in RecommendFiles):
                RecommendFiles.append(name)

        # print (RecommendFiles)
    return RecommendFiles


def main1(uid1):

    data = getData("yonghubiaoqianhuizong.txt")

    deleteFile("yonghubiaoqianhuizong.txt")
    # uid1="44" # 以最后一个用户示例
    # b = 0
    for i in range(len(data)):

        if(data[i][0]==str(uid1)):
            b = data[i]
    uid2=findSimilarity(data,b)
    # print ("最匹配的用户是：" + uid2)

    RecommendFilesID=getRecommendFilesID(uid1,uid2)

    # print (RecommendFilesID)

    fw=open("tuijianwendangdeid.txt","a",encoding="utf-8")
    for i in range (len(RecommendFilesID)):
        fw.write(str(RecommendFilesID[i])+",")
    fw.close()

#######################################################################################################################
def getDataByDB():
    # SQL 查询语句
    sql = "SELECT * FROM zhiku.kw_list "
    # print (sql)
    # 执行SQL语句
    cursor.execute(sql)
    # 获取所有记录列表
    results = cursor.fetchall()
    data = []
    for row in results:
        kws=[]
        for col in row:
            kws.append(col)
        data.append(kws)

    return data


#把标签list转化合并为string，并加在每篇文章的最后一列
def list2String():
    for i in range(len(data)):
        string = ""
        for j in range(1,len(data)-1):
            string=string+data[i][j]
        data[i].append(string)

    # print (data)

#python 两个list 求交集，并集，差集 - CSDN博客  https://blog.csdn.net/bitcarmanlee/article/details/51622263
#获取两个list的交集 返回值是list
def intersection(listA,listB):
    #求交集的两种方式
    retA = [i for i in listA if i in listB]
    # retB = list(set(listA).intersection(set(listB)))
    return retA
#获取两个list的并集 返回值是list
def union(listA,listB):
    #求并集
    retC = list(set(listA).union(set(listB)))
    return retC
#获取两个list的补集 返回值是list
def complement(listA,listB):
    #求差集，在B中但不在A中
    retD = list(set(listB).difference(set(listA)))
    return retD

#获取权重最小的序号index
def getMinIndex(list):
    min=100
    if(len(list)==1):
        index=0
    else:
        for i in range(len(list)):
            if (list[i] < min):
                min = list[i]
                index = i
    return index



#寻找与其最相似的文章
def findSimilarity(data,singleDataAsList):
    # 生成包含匹配数目最多的文章的列表。如[[7, '进程', '内存'], [10, '进程', '内存']]
    allresult = [["初始值"]]
    max = -1
    for i in range(len(data)):
        if (data[i]!=singleDataAsList): #不能与自身比较
            a = data[i]
            intersectionResult = intersection(a,singleDataAsList)
            # print (intersectionResult)
            if (len(intersectionResult) > max and len(intersectionResult) != 0):  # and len(intersectionResult)!=0
                max = len(intersectionResult)
                allresult = [["初始值"]]
                allresult.pop()
                intersectionResult.insert(0, i)
                allresult.append(intersectionResult)
            elif (len(intersectionResult) == max):
                intersectionResult.insert(0, i)
                allresult.append(intersectionResult)

    # 生成相应的列表的权重 如[5, 10]
    allweight = [0] * len(allresult)
    for i in range(len(allresult)):
        for j in range(1, len(allresult[i])):
            for k in range(len(data[allresult[i][0]])):
                if (data[allresult[i][0]][k] == allresult[i][j]):
                    allweight[i] = allweight[i] + k

    # print (allresult)
    # print (allweight)

    minIndex = getMinIndex(allweight)
    title = data[allresult[minIndex][0]][0]

    # print ("最匹配的文章id是：" + str(title))
    fid=title

    return fid

def main2(fid):

    data=getDataByDB()
    # print (data)
    # data=getData("分词结果汇总.csv")  #读取数据库

    for i in range(len(data)):
        if(str(data[i][0])==str(fid)):
            b=data[i]

    # b = data[-1]  # 以最后一篇文章示例
    result= findSimilarity(data,b)

    fw = open("tuijianwendangdeid.txt", "a",encoding="utf-8")
    fw.write(str(result))
    fw.close()

######################################################################################################################
def deleteFile(file):
    #删除文件
    if (os.path.exists(file)):
        os.remove(file)
    # else:
        # print ('不存在要删除的文件:%s' % file)

def export(result):
    for i in range(len(result)):
        for j in range(len(result[i])):
            result[i][j]=str(result[i][j])

    fw = open("/zhiku/recommend_files.json", "w", encoding='utf-8')
    # fw = open("recommend_files.json", "w", encoding='utf-8')
    fw.write("[\n")
    for i in range(len(result) - 1):
        fw.write("{\n")
        fw.write("\"fid\":" + '\"' + result[i][0] + '\"' + ",\n")
        fw.write("\"nickname\":" + '\"' + result[i][1] + '\"' + ",\n")
        fw.write("\"fileinfo\":{\n")
        fw.write("\"name\":" + '\"' + result[i][2] + '\"' + ",\n")
        fw.write("\"course\":" + '\"' + result[i][3] + '\"' + ",\n")
        fw.write("\"dncnt\":" + '\"' + result[i][4] + '\"' + ",\n")
        fw.write("\"uptime\":" + '\"' + result[i][5] + '\"' + ",\n")
        fw.write("\"desc\":" + '\"' + result[i][6] + '\"' + "\n")
        fw.write("}\n")
        fw.write("},\n")
    fw.write("{\n")
    fw.write("\"fid\":" + '\"' + result[-1][0] + '\"' + ",\n")
    fw.write("\"nickname\":" + '\"' + result[-1][1] + '\"' + ",\n")
    fw.write("\"fileinfo\":{\n")
    fw.write("\"name\":" + '\"' + result[-1][2] + '\"' + ",\n")
    fw.write("\"course\":" + '\"' + result[-1][3] + '\"' + ",\n")
    fw.write("\"dncnt\":" + '\"' + result[-1][4] + '\"' + ",\n")
    fw.write("\"uptime\":" + '\"' + result[-1][5] + '\"' + ",\n")
    fw.write("\"desc\":" + '\"' + result[-1][6] + '\"' + "\n")
    fw.write("}\n")
    fw.write("}\n")
    fw.write("]\n")

    fw.close()

def exportEmpty():
    fw = open("/zhiku/recommend_files.json", "w", encoding='utf-8')
    # fw = open("recommend_files.json", "w", encoding='utf-8')
    fw.close()

def main3():
    fr = open("tuijianwendangdeid.txt", "r")
    data = fr.readline()
    data = data.split(",")
    # print (data)
    fr.close()

    deleteFile("tuijianwendangdeid.txt")

    if (data[-1] in data[0:4]):
        resultid = data[0:5]
    else:
        resultid = data[0:4]
        resultid.append(data[-1])

    # return result

    # 查询filesInfo(不含用户昵称nickname)
    sql = "SELECT * FROM zhiku.file_info where fid=" + str(resultid[0])
    for i in range(1,len(resultid)):
       sql=sql +" or fid="+str(resultid[i])
    # print (sql)
    # 执行SQL语句
    cursor.execute(sql)
    # 获取所有记录列表
    results = cursor.fetchall()
    filesInfo=[]

    for row in results:
        uid=row[9]
        name=row[2]
        course=row[4]
        dncnt=row[11]
        uptime=row[10]
        desc=row[13]
        filesInfo.append([uid,name,course,dncnt,uptime,desc])

    # print (filesInfo)

    # # 整理uptime
    for i in range(len(filesInfo)):
        filesInfo[i][4] = filesInfo[i][4].strftime('%Y-%m-%d %H:%M:%S')


    # 查询nickname
    nicknames=[]
    for i in range(len(filesInfo)):
        sql = "SELECT * FROM zhiku.user where uid=" + str(filesInfo[i][0])
        # print (sql)
        # 执行SQL语句
        cursor.execute(sql)
        # 获取所有记录列表
        results = cursor.fetchall()
        for row in results:
            nickname = row[2]
            nicknames.append(nickname)

    #组合fid, filesInfo 和 nickname
    for i in range(len(filesInfo)):

        filesInfo[i][0]=nicknames[i]
        # print (filesInfo[i])
        filesInfo[i].insert(0, resultid[i])
        # print (filesInfo[i])

    # print (filesInfo)

    if(len(filesInfo)!=0):
        export(filesInfo)
    else:
        exportEmpty()

def str2list(string):
    string = string.replace("[","")
    string = string.replace("]", "")
    ls=string.split(",")

    return ls


uidAndFid=sys.argv[1]
uidAndFid=str2list(uidAndFid)

# print (uidAndFid)
# print (type(uidAndFid))
main1(eval(uidAndFid[0])) #用户协同过滤推荐
main2(eval(uidAndFid[1])) #文档内容推荐

# main1(sys.argv[1])
# main2(sys.argv[2])

# main1(44)
# main2(32)
main3()   #得到推荐文档的id



db.close()