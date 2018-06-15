import pytesseract
from PIL import Image
import datetime
import fenci

def str2list(string):
    string = string.replace("[","")
    string = string.replace("]", "")
    ls=string.split(",")

    return ls

def deleteFile(file):
    if (os.path.exists(file)):
        os.remove(file)

def updateDB(result):
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

def png2txt(pathAndName):

    fid=pathAndName[0]
    filePath=pathAndName[1]
    fileName=pathAndName[2]
    # fileType="png"

    isFileExist = True
    while(isFileExist==True):
        i=0
        try:
            image = Image.open(filePath + "/" + fileName + "_" + (i + 1) + ".png")
        except:
            isFileExist = False
        if (isFileExist == True):
            text = pytesseract.image_to_string(image, lang='chi_sim')  # 使用简体中文解析图片
            deleteFile(filePath + "/" + fileName + "_" + (i + 1) + ".png")
            text = text.replace(" ", "")
            with open(filePath + "/" + fileName + "_" + (i + 1) + ".txt", "w") as f:  # 将识别出来的文字存到本地
                f.write(str(text))

            i=i+1

def merge_txt(pathAndName):

    fid = pathAndName[0]
    filePath = pathAndName[1]
    fileName = pathAndName[2]


    text = ""
    isFileExist = True
    while (isFileExist == True):
        i = 0
        try:
            fr = open(filePath + "/" + fileName + "_" + (i + 1) + ".txt")
            fr.close()
        except:
            isFileExist = False
        if (isFileExist == True):
            fr = open(filePath + "/" + fileName + "_" + (i + 1) + ".txt")
            text = text + fr.read()
            fr.close()
            deleteFile(filePath + "/" + fileName + "_" + (i + 1) + ".txt")
            i = i + 1

    fw = open(filePath + "/" + fileName + ".txt", 'a')
    fw.write(text)
    fw.close()


pathAndName=sys.argv[1]
pathAndName=str2list(pathAndName)

png2txt(pathAndName)
merge_txt(pathAndName)
fenci.main(pathAndName[0],pathAndName[1],pathAndName[2])