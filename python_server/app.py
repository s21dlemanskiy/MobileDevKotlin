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
    Song(
            id=5,
            title="Группа крови",
            text="""Теплое место, но улицы ждут
Отпечатков наших ног.
Звездная пыль - на сапогах.
Мягкое кресло, клетчатый плед,
Не нажатый вовремя курок.
Солнечный день - в ослепительных снах.

Группа крови - на рукаве,
Мой порядковый номер - на рукаве,
Пожелай мне удачи в бою,
Пожелай мне:
Не остаться в этой траве,
Не остаться в этой траве.
Пожелай мне удачи,
Пожелай мне удачи!""",
            author_id=2,
        ),

        Song(
            id=6,
            title="Звезда по имени Солнце",
            text="""Белый снег, серый лед,
На растрескавшейся земле.
Одеялом лоскутным на ней -
Город в дорожной петле.
А над городом плывут облака,
Закрывая небесный свет.
А над городом - желтый дым,
Городу две тысячи лет,
Прожитых под светом Звезды
По имени Солнце...""",
            author_id=2,
        ),

        Song(
            id=7,
            title="Коньяк",
            text="""Слово завтра много обещает.
Завтра у тебя есть всё: много полок с вещами.
Тёплый дом и никаких раздумий ночами.
Никаких понятий о печали. Нет!
Мы не обнищали, -
Мы были нищими ещё в самом начале.
Потели, пока не спились или не сторчались -
А завтра, как всегда слишком много обещает.""",
            author_id=4,
        ),

        Song(
            id=8,
            title="Полковнику никто не пишет",
            text="""Полковнику никто не пишет,
Полковника никто не ждет.
Пойдем домой, товарищ мой,
Пойдем домой, пойдем домой.
На линии огня
Пули, что отменяют
Наши имена.""",
            author_id=5,
        ),

        Song(
            id=9,
            title="Domino",
            text="""Domino dancing
All night long
Time to say goodbye
When the morning comes
But if you want to cry
Cry on my shoulder
If you want to fly
Even higher, higher""",
            author_id=6,
        ),
    Song(
        id=10,
        title="Пачка сигарет",
        text="""Я сижу и смотрю в чужое небо из чужого окна
И не вижу ни одной знакомой звезды.
Я ходил по всем дорогам и туда, и сюда,
Обернулся - и не смог разглядеть следы.""",
        author_id=2,
    ),
    Song(
        id=11,
        title="Кукушка",
        text="""Песен еще ненаписанных сколько?
Скажи, кукушка, пропой.
В городе мне жить или на выселках,
Камнем лежать или гореть звездой?""",
        author_id=2,
)
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
    ),
    Author(
        id=4,
        name="Скриптонит"
    ),

    Author(
        id=5,
        name="Би-2"
    ),

    Author(
        id=6,
        name="Domino"
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
