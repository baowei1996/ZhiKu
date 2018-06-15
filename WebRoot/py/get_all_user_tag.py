#encoding=utf-8
import pymysql

# 打开数据库连接
db = pymysql.connect("719daze.me", "jpidea", "jp", "zhiku", charset='utf8')
# 使用cursor()方法获取操作游标
cursor = db.cursor()

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

#通过fids来获取各个文件的标签，返回值是一个二维数组。  keywords
def getKeywords(fids):
   keywords = []

   for i in range(len(fids)):
       # SQL 查询语句
       sql = "SELECT * FROM zhiku.kw_list  where fid=" + str(fids[i])
       # 执行SQL语句
       cursor.execute(sql)
       # 获取所有记录列表
       results = cursor.fetchall()
       if(results != ()):
           keywords.append(list(results))

   #注意此时每行记录仍以一个元组的格式储存着，需要额外转化
   for i in range(len(keywords)):
       keywords[i]=list(keywords[i][0])

   # print (keywords)
   return keywords

#通过传入的二维数组（储存着所有文件的标签），寻找topK个最高频分词。 返回值是一个list。  topKKeywords
def getTopKKeywords(keywords,topK):
    dictOfKeywords = {}
    for i in range(len(keywords)):
        if(i==0):
            for j in range(1, len(keywords[i])):
                if (keywords[i][j] not in dictOfKeywords.keys()):
                    dictOfKeywords[keywords[i][j]] = (16 - j) * 3
                else:
                    dictOfKeywords[keywords[i][j]] = dictOfKeywords[keywords[i][j]] + (16 - j) * 3
        elif (1<=i<=2):
            for j in range(1, len(keywords[i])):
                if (keywords[i][j] not in dictOfKeywords.keys()):
                    dictOfKeywords[keywords[i][j]] = (16 - j) * 2
                else:
                    dictOfKeywords[keywords[i][j]] = dictOfKeywords[keywords[i][j]] + (16 - j) * 2
        else:
            for j in range(1, len(keywords[i])):
                if (keywords[i][j] not in dictOfKeywords.keys()):
                    dictOfKeywords[keywords[i][j]] = (16 - j) * 1
                else:
                    dictOfKeywords[keywords[i][j]] = dictOfKeywords[keywords[i][j]] + (16 - j) * 1

    dictOfKeywords = dict(sorted(dictOfKeywords.items(), key=lambda dictOfKeywords: dictOfKeywords[1], reverse=True))
    topKKeywords=list(dictOfKeywords.keys()) [0:topK] #注意最后要转化为list格式

    # return dictOfKeywords
    return topKKeywords

#通过uid来获取一个用户的topK个标签。返回值是一个list。  uidKeywords
def getUidKeywords(uid):
    fids = getFidsByUid(uid)
    keywords = getKeywords(fids)
    topKKeywords = getTopKKeywords(keywords, 15)
    topKKeywords.insert(0,uid)
    uidKeywords=topKKeywords
    # print (uidKeywords)
    return uidKeywords

#获取所有的file_op（文件下载上传记录表）的用户的id，返回值是一个list。   uids
def getAllUid():
    # SQL 查询语句
    sql = "SELECT uid FROM zhiku.file_op   where (type=1 or type=0 ) "
    # print (sql)
    # 执行SQL语句
    cursor.execute(sql)
    # 获取所有记录列表
    results = cursor.fetchall()
    uids = []
    for row in results:
        uid = row[0]
        # 打印结果
        if (uid not in uids):
            uids.append(uid)

    # print (uids)
    return uids

#通过uids来获取所有用户的喜好标签，返回值是一个二维数组。  allUidTopKKeywords
def getAllUidTopKKeywords(uids):
    allUidTopKKeywords=[]
    for i in range(len(uids)):
        singleUidTopKKeywords=getUidKeywords(uids[i])
        if(len(singleUidTopKKeywords)>=2):
            allUidTopKKeywords.append(singleUidTopKKeywords)

    return allUidTopKKeywords

#导出文件  用户标签汇总.txt
def export(allUidTopKKeywords):
    fw = open("yonghubiaoqianhuizong.txt", "w",encoding="utf-8") #,encoding="utf-8"
    for i in range(len(allUidTopKKeywords)):
        for j in range(len(allUidTopKKeywords[i]) - 1):
            fw.write(str(allUidTopKKeywords[i][j]) + ",")
        fw.write(str(allUidTopKKeywords[i][-1]) + "\n")
    fw.close()

def main():
    uids = getAllUid()
    allUidTopKKeywords = getAllUidTopKKeywords(uids)
    export(allUidTopKKeywords)

    db.close()

main()