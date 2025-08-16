import numpy as np
import pandas as pd
import requests
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver import ActionChains
import sqlite3
import sqlite3


for i in range(2, 50):
    TotalData = []
    url = 'https://www.yes24.com/24/category/bestseller?CategoryNumber=001&sumgb=06&fetchSize=40&PageNumber=' + str(i)
    #url = 'http://www.yes24.com/Product/Goods/71933018'
    print(url)

    res = requests.post(url)
    soup = BeautifulSoup(res.text, 'html5lib')

    t_bodyList = soup.find('body', id_='#category_layout')
    #print(t_bodyList)

    table = soup.find('table')
    #print(table)
    ids = soup.select('#category_layout > tbody > tr')
    #menu = soup.select('#category_layout > tbody > tr')
    j = 0
    for id in ids:
        img = id.find('a')
        #print(img)
        if img != None:
            bookid = img.get('href')
            bookid = str(bookid)
            bookid = bookid.split('/')
            if bookid[3] != 'campaign' and 'eWorld':
                finalId = bookid[3]
                #print(finalId)

                goodsUrl = 'https://www.yes24.com/Product/Goods/' + finalId
                res2 = requests.post(goodsUrl)
                soup2 = BeautifulSoup(res2.text, 'html5lib')

                bookName = soup2.select('#yDetailTopWrap > div.topColRgt > div.gd_infoTop > div > h2')

                group = []
                for name in bookName:
                    finalbookName = name.get_text()
                    print(finalbookName)
                    group.append(finalbookName)
                #bookName = bookName.get_text()
                #print(bookName)

                book_info = soup2.select('#infoset_introduce > div.infoSetCont_wrap > div > div > textarea')
                print(book_info)
                for info in book_info:
                    a = info.get_text()
                    print(a)
                    #print(a)
                    if a == '':
                        NoneString = 'None'
                        group.append(NoneString)

                    else:
                        a = a.replace('<br>', '')
                        a = a.replace('<br/>', '')
                        a = a.replace('</br>', '')
                        a = a.replace('<b>', '')
                        a = a.replace('</b>', '')
                        a = a.replace('<B>', '')
                        a = a.replace('</B>', '')
                        print(a)
                        group.append(a)

                TotalData.append(group)

    print(TotalData)


    # 데이터베이스 연결
    conn = sqlite3.connect('./DSLibrary.db')

    # 커서 생성
    cursor = conn.cursor()
    for i in range(len(TotalData)):
        datalen = len(TotalData[i])
        if datalen == 1:
            nameBook = TotalData[i][0]
            cursor.execute("INSERT INTO book (bookName, bookInform) VALUES (?, ?)",
                           (nameBook, 'NULL'))
        elif datalen == 2:
            nameBook = TotalData[i][0]
            infoBook = TotalData[i][1]
            cursor.execute("INSERT INTO book (bookName, bookInform) VALUES (?, ?)",
                           (nameBook, infoBook))
        conn.commit()


        #img = id.find('div', class_='goodsImgW')

'''
    selected_elements = soup.select('div.view_type_list.switch_prod_wrap > ol > li')
    print(selected_elements)
    for element in selected_elements:
        classes = element.get('class', [])  # get 메서드를 사용하여 'class' 속성 값을 가져옵니다.
        print(classes)
    div_tags = soup.find_all(class_="prod_item")
    print(div_tags)

    for div in div_tags:
        class_names = div.get('data-id')
        if class_names:  # class 속성이 있는 경우에만 출력
            print(class_names)

        book_id = soup.select('#tabRoot > div.view_type_list.switch_prod_wrap > ol:nth-child(1) > li:nth-child(1)')
        item_id = book_id['itemid']
        print(item_id)
        sub_url = 'https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=' + book_id
        tag_name = '#category_layout > tbody > tr:nth-child(' + str(j) + ') > td.goodsTxtInfo > p:nth-child(1) > a:nth-child(1)'
        books = soup.select(tag_name)

        book_name = books[0].text
        print(book_name)
        cnt += 1
        j += 2
'''