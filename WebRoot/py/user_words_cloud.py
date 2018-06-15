# encoding=utf-8
import io
import sys
import pymysql

'''
词云图
{
            text: '111',
            value: Math.random(),
            category: 'a'
        },
        {
            text: '333',
            value: Math.random(),
            category: 'b'
        },

'''
# 打开数据库连接
db = pymysql.connect("719daze.me", "jpidea", "jp", "zhiku", charset='utf8')


# 通过uid获取一个用户所下载或上传过的所有文件，fids，返回值是一个list。  fids
def getFidsByUid(uid):
    # 使用cursor()方法获取操作游标
    cursor = db.cursor()
    # SQL 查询语句
    sql = "SELECT * FROM zhiku.file_op   where (type=1 or type=0 ) and uid=" + str(uid) + " order by optime desc"
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


# 通过fids来获取各个文件的标签，返回值是一个二维数组。  keywords
def getKeywords(fids):
    keywords = []
    # 使用cursor()方法获取操作游标
    cursor = db.cursor()
    for i in range(len(fids)):
        # SQL 查询语句
        sql = "SELECT * FROM zhiku.kw_list  where fid=" + str(fids[i])
        # 执行SQL语句
        cursor.execute(sql)
        # 获取所有记录列表
        results = cursor.fetchall()
        if (results != ()):
            keywords.append(list(results))

    # 注意此时每行记录仍以一个元组的格式储存着，需要额外转化
    for i in range(len(keywords)):
        keywords[i] = list(keywords[i][0])

    # print (keywords)
    return keywords


# 通过传入的二维数组（储存着所有文件的标签），寻找topK个最高频分词。 返回值是两个list。  topKKeywords,topKValues
def getTopKKeywords(keywords, topK):
    dictOfKeywords = {}
    for i in range(len(keywords)):
        if (i == 0):
            for j in range(1, len(keywords[i])):
                if (keywords[i][j] not in dictOfKeywords.keys()):
                    dictOfKeywords[keywords[i][j]] = (16 - j) * 3
                else:
                    dictOfKeywords[keywords[i][j]] = dictOfKeywords[keywords[i][j]] + (16 - j) * 3
        elif (1 <= i <= 2):
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
    topKKeywords = list(dictOfKeywords.keys())[0:topK]  # 注意最后要转化为list格式
    topKValues = list(dictOfKeywords.values())[0:topK]  # 注意最后要转化为list格式
    # return dictOfKeywords
    return topKKeywords, topKValues


# 通过uid来获取一个用户的topK个标签。返回值是两个list。  uidKeywords,uidKeywordValues
def getUidKeywords(uid):
    fids = getFidsByUid(uid)
    keywords = getKeywords(fids)
    topKKeywords, topKValues = getTopKKeywords(keywords, 15)
    topKKeywords.insert(0, uid)
    topKValues.insert(0, uid)

    uidKeywords = topKKeywords
    uidKeywordValues = topKValues
    # print (uidKeywords)
    return uidKeywords, uidKeywordValues


# 导出文件  用户标签汇总.txt
def export(uidKeywords, uidKeywordValues):
    fw = open(r"/zhiku/user_words_cloud.json", "w", encoding='utf-8')
    fw.write("[\n")
    for i in range(1, len(uidKeywords) - 1):
        fw.write("{\n")
        fw.write("\"text\":" + '\"' + str(uidKeywords[i]) + '\"' + ",\n")
        fw.write("\"value\":" + '\"' + str(uidKeywordValues[i]) + '\"' + ",\n")
        fw.write("\"category\":" + '\"' + "a" + '\"' + "\n")
        fw.write("},\n")
    fw.write("{\n")
    fw.write("\"text\":" + '\"' + str(uidKeywords[-1]) + '\"' + ",\n")
    fw.write("\"value\":" + '\"' + str(uidKeywordValues[-1]) + '\"' + ",\n")
    fw.write("\"category\":" + '\"' + "a" + '\"' + "\n")
    fw.write("}\n")
    fw.write("]\n")
    fw.close()

def exportEmpty():
    fw = open(r"/zhiku/user_words_cloud.json", "w", encoding='utf-8')
    fw.close()


def main(uid):
    uidKeywords, uidKeywordValues = getUidKeywords(uid)
    #print (uidKeywords)
    #print (uidKeywordValues)

    if(len(uidKeywords)==1):
        exportEmpty()
    else:
        export(uidKeywords, uidKeywordValues)
    # 关闭数据库连接
    db.close()


main(sys.argv[1])
