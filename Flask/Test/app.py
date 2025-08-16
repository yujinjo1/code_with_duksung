from flask import Flask
from flask_restful import Resource, Api, reqparse, abort
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from soynlp.noun import LRNounExtractor_v2
from joblib import dump, load
import sqlite3
import json

app = Flask(__name__)

api = Api(app)


bookData = []
keyword = ['사랑']


stopwords = \
    '않다 되어다 되다 하다 어떻다 이렇다 이다 어제 매일 아 휴 아이구 아이쿠 아이고 어 나 우리 저희 따라 의해 을 를 에 의 가 으로 로 에게 뿐이다 의거하여 근거 입각하여 기준으로 에하면 예를 들면 들자면 저 소인 소생 하지마 하지마라 다른 물론 또한 그리고 비길 수 없다 뿐만 만이 만은 막론 관계없이 그치지 않다 그러나 그런데 든간에 논하지 설사 따지지 비록 더라도 아니면 만 못하다 하는 편이 낫다 불문하고 향하여 향해서 향하다 쪽으로 틈타 이용하여 타다 오르다 제외하고 이 외에 밖에 하여야 비로소 한다면 몰라도 외에도 이곳 여기 부터 기점으로  따라서 할 생각이다 하려고하다 이리하여 그리하여 그렇게 함으로써 하지만 일때 할때 앞에서 중에서 보는데서 으로써 로써 까지 해야한다 일것이다 반드시 할줄알다 할수있다 할수있어 임에 틀림없다 한다면 등 등등 제 겨우 단지 다만 할뿐 딩동 댕그 대해서 대하여 대하면 훨씬 얼마나 얼마만큼 얼마큼 남짓 여 얼마간 약간 다소 좀 조금 몇 얼마 지만 하물며 또한 그러나 그렇지만 하지만 일단 한켠으로는 오자마자 이렇게되면 이와같다면 전부 한마디 한항목 근거로 하기에 아울러 로 인하여 까닭으로 이유만으로 이로 인하여 그래서 이쪽 여기 이것 이번 이렇게말하자면 이런 이러한 이와 같은 요만큼 요만한 것 얼마 안 되는 것 이만큼 이 정도의 이렇게 많은 것 이와 같다 이때 이렇구나 것과 같이 끼익 삐걱 따위 와 같은 사람들 부류의 사람들 왜냐하면 중의하나 오직 오로지 에 한하다 하기만 하면 도착하다 까지 미치다 도달하다 정도에 이르다 할 지경이다 결과에 이르다 관해서는 여러분 하고 있다 한 후 혼자 자신 우에 종합한것과같이 총적으로 보면 총적으로 말하면 총적으로 대로 하다 으로서 참 그만이다 할 따름이다 봐 봐라 아이야 아니 륙 칠 팔 구 둘 셋 넷 다섯 여섯 일곱 여덟 아홉 령 영'
stopwords = stopwords.split(' ')

# 데이터베이스 연결 생성 (파일이 없으면 새로운 데이터베이스 파일이 생성됩니다)
conn = sqlite3.connect('/Users/violet/DSLibrary.db')
# 커서 생성
cursor = conn.cursor()


# 변경사항 커밋
conn.commit()

# 데이터 조회
cursor.execute("SELECT bookName FROM book;")
bookNm = cursor.fetchall()
cursor.execute("SELECT bookGenre FROM book;")
bookGr = cursor.fetchall()
cursor.execute("SELECT bookInform FROM book;")
bookIf = cursor.fetchall()

for i in range(len(bookNm)):
    books = {}
    bookdescription = ""
    if bookIf == 'NULL':
        bookIf = ''
    bookdescription = str(bookNm[i][0]) + str(bookGr[i][0]) + str(bookIf[i][0])
    books["name"] = bookNm[i][0]
    books["description"] = bookdescription
    bookData.append(books)

#print(bookData)



book_descriptions = [book["description"] for book in bookData]
descriptions = book_descriptions

# 명사 추출기
noun_extractor = LRNounExtractor_v2()
noun_extractor.train(descriptions)
nouns = noun_extractor.extract()

def extract_nouns(text):
    #tokens = [word for word, score in nouns.items() if word in text]
    tokens = [word for word, score in nouns.items() if word in text and word not in stopwords]
    return " ".join(tokens)


all_book_nouns = [extract_nouns(book["description"]) for book in bookData]
#print(all_book_nouns)

tfidf_vectorizer = TfidfVectorizer()
tfidf_matrix = tfidf_vectorizer.fit_transform(all_book_nouns)

# 사용자가 들은 강의
# 애플리케이션에서는 사용자의 기록을 기반
#print(user_book_nouns)
user_tfidf = tfidf_vectorizer.transform(keyword)

user_profile = user_tfidf.mean(axis=0)
cosine_similarities = cosine_similarity(user_profile, tfidf_matrix)

recommendations = []
sorted_indices = cosine_similarities.argsort().flatten()[::-1]

for idx in sorted_indices:
    recommendations.append(bookData[idx]["name"])

result = []
print("추천 도서:")
for rec in recommendations[:3]:  # 강의 몇 개 추천
    result.append(rec)
    print(rec)

# 데이터베이스 연결 종료
conn.close()

data = {
    'todo1': {"task": result[0]},
    'todo2': {"task": result[1]},
    'todo3': {"task": result[2]}
}

final_json_result = json.dumps(data)
print(final_json_result)


class Recommendation(Resource):
    def get(self):
        return final_json_result

api.add_resource(Recommendation, '/recomm/')

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5001, debug=False)