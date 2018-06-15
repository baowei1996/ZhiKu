# 通过关键词爬取csdn博客文章
import io
import sys
import requests
from bs4 import BeautifulSoup
import pymysql

# 打开数据库连接
db = pymysql.connect("719daze.me", "jpidea", "jp", "zhiku", charset='utf8')


def getHTMLText(url):  # 作用：得到html的text
    try:
        r = requests.get(url, timeout=30)
        r.raise_for_status
        r.encoding = "utf-8"
        return r.text
    except:
        # print ("getHTMLText出现异常")
        return "getHTMLText出现异常"


def getInformation(soup):  # 作用：将html的有用信息筛选出来并储存到相对应的列表alist中
    result = []
    # 通过查看网页源代码，分析得到下面的解析特点。
    data = soup.find_all("dl")  # 每个dl里面储存着一篇csdn博客的信息，1个dl里有1个dt和3个dd
    for dl in data:
        ldt = dl.find_all("dt")  # dt里储存着博客的题目
        for dt in ldt:
            # print (type(dt.get_text()))
            text = dt.get_text()
            # print (text)
            indexOfStart = text.find("\n")
            indexOfEnd = text.find("- CSDN博客")
            # print (indexOfStart)
            # print (indexOfEnd)
            title = text[indexOfStart:indexOfEnd - 3].replace("\n", "")
            #print ("标题是：" + title)
            # print ("@@@@@@@@@@@@@@@@@@@@@@@@@@@")
        ldd = dl.find_all("dd")  # 1个dl里有3个dd，分别是作者日期浏览次数，简介，链接
        # 作者日期浏览次数
        text = ldd[0].get_text()
        indexOfStart = text.find("作者")
        indexOfEnd = text.find("日期")
        auth = text[indexOfStart + 3:indexOfEnd - 3]
        #print ("作者是：" + auth)

        indexOfStart = text.find("日期")
        indexOfEnd = text.find("浏览")
        date = text[indexOfStart + 3:indexOfEnd - 3]
        #print ("日期是：" + date)

        text = ldd[0].get_text()
        indexOfStart = text.find("浏览")
        indexOfEnd = text.find("次")
        scancnt = text[indexOfStart + 3:indexOfEnd - 1]
        #print ("浏览次数是：" + scancnt)

        # 简介
        text = ldd[1].get_text()
        text = text.replace("\n", "")
        text = text.replace("\t", "")
        text = text.replace(" ", "")
        # text=text[0:50]
        descs = text
        #print ("简介是：" + descs)

        # 链接
        text = ldd[2].get_text()
        url = text
        #print ("链接是：" + url)

        result.append([title, date, auth, url, scancnt, descs])
        #print ("**********************************************************")

    return result


def compareStr(a, b):
    sum = 0
    for i in range(len(a)):
        for j in range(len(b)):
            if (a[i] == b[j]):
                sum = sum + 1
    if (a <= b):
        sumPercent = float(sum / len(a))
    else:
        sumPercent = float(sum / len(b))
    return sumPercent

def compareStr2(a, b):
    sum = 0
    for i in range(len(a)):
        for j in range(len(b)):
            if (a[i] == b[j]):
                sum = sum + 1
    if (a <= b):
        sumPercent = float(sum / len(b))
    else:
        sumPercent = float(sum / len(a))
    return sumPercent

def isSame(a,b):
    same=True
    if (0.7 > compareStr(a,b)):  # and len(newResult[ni][0])>=5
        same = False
    elif(0.7 < compareStr(a,b) and compareStr2(a,b)<0.8):
        same = False
    return same

def filter(result):
    newResult = []
    newResult.append(result[0])
    for i in range(len(result)):
        same = False
        for ni in range(len(newResult)):
            if (len(newResult) >= 5):
                same = True
            if (isSame(newResult[ni][0], result[i][0]) ==True):
                same = True
        if (same == False):
            newResult.append(result[i])
    return newResult

def export(result):
    fw = open(r"/zhiku/csdn.json", "w", encoding='utf-8')
    fw.write("[\n")
    for i in range(len(result) - 1):
        fw.write("{\n")
        fw.write("\"title\":" + '\"' + result[i][0] + '\"' + ",\n")
        fw.write("\"date\":" + '\"' + result[i][1] + '\"' + ",\n")
        fw.write("\"auth\":" + '\"' + result[i][2] + '\"' + ",\n")
        fw.write("\"url\":" + '\"' + result[i][3] + '\"' + ",\n")
        fw.write("\"scancut\":" + '\"' + result[i][4] + '\"' + "\n")
        # fw.write("\"descs\":" + '\"'+ result[i][5] + '\"'+ "\n")
        fw.write("},\n")
    fw.write("{\n")
    fw.write("\"title\":" + '\"' + result[-1][0] + '\"' + ",\n")
    fw.write("\"date\":" + '\"' + result[-1][1] + '\"' + ",\n")
    fw.write("\"auth\":" + '\"' + result[-1][2] + '\"' + ",\n")
    fw.write("\"url\":" + '\"' + result[-1][3] + '\"' + ",\n")
    fw.write("\"scancut\":" + '\"' + result[-1][4] + '\"' + "\n")
    # fw.write("\"descs\":" + '\"'+ result[i][5] + '\"'+ "\n")
    fw.write("}\n")
    fw.write("]\n")
    fw.close()

def exportEmpty():
    fw = open(r"/zhiku/csdn.json", "w", encoding='utf-8')
    fw.close()


def getKeywords(fid):
    keywords = []
    # 使用cursor()方法获取操作游标
    cursor = db.cursor()
    # SQL 查询语句
    sql = "SELECT * FROM zhiku.kw_list  where fid=" + str(fid)
    # 执行SQL语句
    cursor.execute(sql)
    # 获取所有记录列表
    results = cursor.fetchall()
    if (results != ()):
        keywords = (list(results))

    # 注意此时每行记录仍以一个元组的格式储存着，需要额外转化
    for i in range(len(keywords)):
        keywords = list(keywords[0])

    keywords = keywords[1:4]
    return keywords


def main(fid):
    a = getKeywords(fid)
    # a=["线程","地址"]
    # a=["yanjiaxin8410"]
    keyword = ""
    if (len(a) >= 2):
        for i in range(len(a) - 1):
            keyword = keyword + a[i] + "+"
        keyword = keyword + a[-1]
    else:
        keyword = keyword + a[0]

    html1 = getHTMLText(r"https://so.csdn.net/so/search/s.do?p=1&q=" + keyword + "&t=blog&domain=&o=&s=&u=&l=&f=&rbg=0")
    html2 = getHTMLText(r"https://so.csdn.net/so/search/s.do?p=2&q=" + keyword + "&t=blog&domain=&o=&s=&u=&l=&f=&rbg=0")
    soup1 = BeautifulSoup(html1, "html.parser")
    result1 = getInformation(soup1)
    soup2 = BeautifulSoup(html1, "html.parser")
    result2 = getInformation(soup2)

    result = result1 + result2
    if (len(result) != 0):
        newresult = filter(result)
        export(newresult)
    else:
        exportEmpty()


main(sys.argv[1])

db.close()
