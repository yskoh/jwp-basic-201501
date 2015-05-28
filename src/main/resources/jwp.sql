DROP TABLE IF EXISTS QUESTIONS;

CREATE TABLE QUESTIONS (
	questionId 			bigint				auto_increment,
	writer				varchar(30)			NOT NULL,
	title				varchar(100)			NOT NULL,
	contents			varchar(5000)		NOT NULL,
	createdDate			timestamp			NOT NULL,
	countOfComment		int,
	PRIMARY KEY               (questionId)
);

DROP TABLE IF EXISTS ANSWERS;

CREATE TABLE ANSWERS (
	answerId 			bigint				auto_increment,
	writer				varchar(30)			NOT NULL,
	contents			varchar(5000)		NOT NULL,
	createdDate			timestamp			NOT NULL,
	questionId			bigint				NOT NULL,				
	PRIMARY KEY         (answerId)
);

INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfComment) VALUES
('javajigi',
'javascript 학습하기 좋은 라이브러리를 추천한다면...', 
'이번 slipp에서 진행하는 5번째 스터디 주제가 trello의 아키텍처를 분석하고, trello에서 사용하는 기술을 학습하는 과정이다. 이 아이디어로 스터디를 진행하게 된 계기는 http://www.mimul.com/pebble/default/2014/03/17/1395028081476.html 글을 보고 스터디 주제로 진행해 보면 좋겠다는 생각을 했다.
이번 스터디를 진행하면서 가장 힘든 점이 javascript라 생각한다. 지금까지 javascript를 사용해 왔지만 깊이 있게 사용한 경험은 없기 때문에 이번 기회에 틈틈히 학습해 보려고 생각하고 있다. 단, 학습 방법을 지금까지와는 다르게 오픈 소스 라이브러리 중에서 괜찮은 놈을 하나 골라 소스 코드를 읽으면서 학습하는 방식으로 진행해 보려고 한다. 아무래도 책 한권을 처음부터 읽으면서 단순 무식하게 공부하기 보다는 이 방식이 좋지 않을까라는 기대 때문이다. javascript의 개발 스타일도 이해할 수 있기 때문에 좋은 접근 방법이라 생각한다.
혹시 javascript를 학습하기 좋은 라이브러리가 있을까? 소스 코드 라인 수가 많지 않으면서 소스 코드 스타일에서도 배울 점이 많은 그런 라이브러리면 딱 좋겠다.',
CURRENT_TIMESTAMP(), 2);

INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES
('eungju',
'http://underscorejs.org/docs/underscore.html Underscore.js 강추합니다!
쓸일도 많고, 코드도 길지 않고, 자바스크립트의 언어나 기본 API를 보완하는 기능들이라 자바스크립트 이해에 도움이 됩니다. 무엇보다 라이브러리 자체가 아주 유용합니다.', 
CURRENT_TIMESTAMP(), 1);

INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES
('Hanghee Yi',
'언더스코어 강력 추천드려요.
다만 최신 버전을 공부하는 것보다는 0.10.0 버전부터 보는게 더 좋더군요.
코드의 변천사도 알 수 있고, 최적화되지 않은 코드들이 기능은 그대로 두고 최적화되어 가는 걸 보면 재미가 있습니다 :)', 
CURRENT_TIMESTAMP(), 1);

INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfComment) VALUES
('자바지기',
'anonymous inner class는 final 변수에만 접근해야 하는 이유는?', 
'오늘 자바 8에 추가된 람다와 관련한 내용을 읽다가 다음과 같이 내용이 있어 궁금증이 생겼다.
람다 표현식에서 변수를 변경하는 작업은 스레드에 안전하지 않다. - 가장 빨리 만나는 자바8 28페이지...
람다 표현식을 이전 버전의 anonymous inner class와 같은 용도로 판단했을 때 기존의 anonymous inner class에서도 final 변수에만 접근할 수 있었다.
지금까지 anonymous inner class에서 final 변수로 정의하는 이유가 현재 method의 Context가 anonymous inner class 인스턴스까지 확대되기 때문에 anonymous inner class 내에서 값을 변경할 경우 그에 따른 side effect가 생길 가능성이 많아 final로 정의하는 것으로 생각했다.
그런데 위 내용은 스레드에 안전하지 않기 때문에 람다 표현식에서 변수 값을 변경하는 것을 막는다고 이야기하고 있다. 왜 스레드에 안전하지 않은 것일까?',
CURRENT_TIMESTAMP(), 3);

INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES
('jhindhal.jhang',
'Thread safe 랑 final은 관계가 있는거지만 다르게 봐야 하는게 아닌가?

굳이 lambda로 변수를 쓸 때 final 지정을 하지 않은 변수 더라도 final효과처럼 사용 한다면 (읽기만 한다 던지...) 사용 가능 하니

Final 과 꼭 lambda를 연관 하지 말고 thread safe하게 프로그래밍 하기 위해 final(또는 final처럼)을 해야 한다는 의미가 아닐까? 생각 하는데...', 
CURRENT_TIMESTAMP(), 2);

INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES
('강우',
'저도 잘은 모르겠지만, 그냥 몇글자 적어볼께요.
일단 변수의 생명 주기랑, 값이 아닌 레퍼런스에 의한 부수효과는 무시하고,
쓰레드 관점에서만 볼때에,
간단히 생각하면, 서블릿에서 인스턴스 변수를 사용하는 것은 쓰레드에 안전할까요? 안전하지 않을까요?
저는 같은 맥락인거 같은데 ^^;;
아 그러고 보니 "안정"이라고 되어있네요. 저건 다른 의미인가.. ^^;;', 
CURRENT_TIMESTAMP(), 2);

INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES
('Toby Lee',
'람다식에서 사용되는 변수라면 람다식 내부에서 정의된 로컬 변수이거나 람다식이 선언된 외부의 변수를 참조하는 것일텐데, 전자라면 아무리 변경해도 문제될 이유가 없고, 후자는 변경 자체가 허용이 안될텐데. 이 설명이 무슨 뜻인지 이해가 안 됨.', 
CURRENT_TIMESTAMP(), 2);