import jieba
import datetime
import os #用来删除临时生成的docx
import pymysql


def readTxt(strFile):
    try:
        file = open(strFile).read()
    except:
        file = open(strFile, encoding="utf-8").read()
    txt=file
    return txt

def segment(doc):
    '''
    用jieba分词对输入文档进行分词，并保存至本地（根据情况可跳过）
    '''
    seg_list = " ".join(jieba.cut(doc, cut_all=False)) #seg_list为str类型
    i=0
    # document_after_segment = open('分词结果.txt', 'a+')
    # document_after_segment.write(seg_list)
    # document_after_segment.close()

    return seg_list

def removeStopWords(seg_list):
    '''
    自行下载stopwords1893.txt停用词表，该函数实现去停用词
    '''
    wordlist_stopwords_removed = []

    stop_words = open('stopwords1893.txt')
    stop_words_text = stop_words.read()

    stop_words.close()

    stop_words_text_list = stop_words_text.split('\n')
    for i in range(len(stop_words_text_list)):
        stop_words_text_list[i]=stop_words_text_list[i].strip()

    # print (len(stop_words_text_list))
    # print (stop_words_text_list)
    after_seg_text_list = seg_list.split(' ')
    # print (len(after_seg_text_list))
    for word in after_seg_text_list:
        if (word not in stop_words_text_list  and len(word)>=2):
            wordlist_stopwords_removed.append(word)

    # print (len(wordlist_stopwords_removed))
    # without_stopwords = open('分词结果(去停用词).txt', 'a+')
    # without_stopwords.write(' '.join(wordlist_stopwords_removed))
    # without_stopwords.close()
    return ' '.join(wordlist_stopwords_removed)

def wordCount(segment_list,resultFileName):
    '''
        该函数实现词频的统计，并将统计结果存储至本地。
        在制作词云的过程中用不到，主要是在画词频统计图时用到。
    '''
    word_lst = []
    word_dict = {}
    word_lst.append(segment_list.split(' '))
    for item in word_lst:
        for item2 in item:
            if item2 not in word_dict:
                word_dict[item2] = 1
            else:
                word_dict[item2] += 1

    word_dict_sorted = dict(sorted(word_dict.items(), key=lambda item: item[1], reverse=True))  # 按照词频从大到小排序

    result=[]
    i=0
    keyOfTop=15
    for key in word_dict_sorted:
        result.append(key)
        i=i+1
        if(i>=keyOfTop):
            break

    return result

def updateDB(result):
    # 打开数据库连接
    db = pymysql.connect("719daze.me", "jpidea", "jp", "zhiku", charset='utf8')
    # 使用cursor()方法获取操作游标
    cursor = db.cursor()
    #将分词结果更新到数据库

    sql = "INSERT INTO zhiku.kw_list VALUES (" + str(result[0]) + ","
    for i in range(1, len(result) - 1):
        sql = sql + '\"' + str(result[i]) + '\"' + ","
    sql = sql + '\"' + str(result[-1]) + '\"' + ")"
    # print (sql)
    try:
        # 执行sql语句
        cursor.execute(sql)
        # 提交到数据库执行
        db.commit()
    except:
        # 如果发生错误则回滚
        db.rollback()
    # print ("更新完成")
    db.close()

def main(fid,filePath,fileName):

    doc = readTxt(filePath + "/" + fileName + ".txt")

    seg_list = segment(doc)
    seg_list2 = removeStopWords(seg_list)
    result = wordCount(seg_list2, filePath + "\\" + fileName)

    result.insert(0, fid)

    updateDB(result)


main(fid,filePath,fileName)

