from flask import Flask, json
from DataBase import Database
from Song import Song
from Author import Author

app = Flask("test server")


database: Database = {
"songs": [
    Song(
        id=1,
        title="Думай позитивно",
        text="""Думай позитивно, стакан всегда наполовину полон, всегда
Чувствуй хорошее, плохого не существует, между \"нет\" и \"да\" выбор только \"да\"
Верь в лучшее, жизнь - это танец под присмотром чуткого Бога
Повторяй эту поебень чаще, повторяй, даже если звучит убого""",
        author_id=1,
    ),
    Song(
        id=2,
        title="Атаман",
        text="""А потом наступит день, день
Каждый скажет то, что было, не помню
И пойдём мы под пастушью свирель
Дружным стадом на бойню""",
        author_id=2,
    ),
    Song(
        id=3,
        title="Гимн панков",
        text="""Там, где застряли немецкие танки
Пройдут свободно русские панки
Ты раздави меня хоть танком
Я всё равно буду панком""",
        author_id=3,
    ),
    Song(
        id=4,
        title="Сказка",
        text="""А потом, придет она.
Собирайся, - скажет, - пошли, отдай земле тело.
Ну, а тело не допело чуть-чуть.
Ну, а телу недодали любви. Странное дело.
Там за окном сказка с несчастливым концом.
Странная сказка.""",
        author_id=2,
    ),
],
"authors": [
    Author(
        id=1,
        name="Кровосток"
    ),
    Author(
        id=2,
        name="Кино"
    ),
    Author(
        id=3,
        name="Красная плесень"
    )
]
}

@app.route('/songs', methods=['GET'], endpoint='songs')
def list1():
    response = app.response_class(
        response=json.dumps(database["songs"]),
        status=200,
        mimetype='application/json'
    )
    return response


@app.route('/authors', methods=['GET'], endpoint='authors')
def list2():
    response = app.response_class(
        response=json.dumps(database["authors"]),
        status=200,
        mimetype='application/json'
    )
    return response

# тк запрос иденпатентен и телом запроса мы не пользуемся сделал его GET а не POST
@app.route('/remove/author/<int:id>', methods=['GET'], endpoint='rm authors')
def rm1(id):
    database["authors"] = list(filter(lambda author: author.id != id, database["authors"]))
    response = app.response_class(
        response=json.dumps(database["authors"]),
        status=200,
        mimetype='application/json'
    )
    return response

# тк запрос иденпатентен и телом запроса мы не пользуемся сделал его GET а не POST
@app.route('/remove/song/<int:id>', methods=['GET'], endpoint='rm songs')
def rm2(id):
    database["songs"] = list(filter(lambda song: song.id != id, database["songs"]))
    response = app.response_class(
        response=json.dumps(database["songs"]),
        status=200,
        mimetype='application/json'
    )
    return response

if __name__ == "__main__":
    # app.run(debug=True, port=80)
    app.run(port=8181)
