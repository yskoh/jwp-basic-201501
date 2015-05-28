#### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.
*	맨 처음에 web.xml 파일을 읽는다.
*	Servlet Context 객체를 만든다.
*	web.xml의 초기화 파라미터들을 객체에 설정한다.
*	Servlet container는 context가 초기화 또는 종료될 event를 발생시는데 이 때 listener로 등록된 객체들에게 event가 발생했다고 알려준다. (event 발생햇는지 알아야하는 객체는 listener로 annotation통해 등록해둘 수 있다)
*	Servlet container는 컨텍스트가 초기화되는 시점에 이벤트를 발생시키고 그 시점에 listener로 등록되어 있는 listener의 contextInitialized() 메쏘드를 실행시킨다. 이 과정에서 DB를 초기화 한다.
*	서블릿 클래스 찾고 로드하여 init()을 실행시킨다.
*	Servlet container에 클라이언트가 요청을 보내면 servlet container init() 의해 초기화된 서블릿의 service()를 실행한다.
*	service()는 요청마다의 새 스레드를 만들고 클라이언트의 request 방식을 확인하여 doGet(), doPost() etc. 를 실행한다.  


#### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.
*	http://localhost:8080를 치면 local port 8080으로 연결을 요청한다. 
tomcat의 경우, welcome-file이 설정되어 있지 않아, 바로 index.jsp로 연결된다.  
index.jsp로 가면 아무 추가 로직 없이, 바로 /list.next로 redirect된다.
list.next로 다시 서버에 연결하면, 요청이 Dispatcher Servlet으로 간다. 
DispatcherServlet에 있는 init()을 통해, RequestMapping이 초기화되고, service 함수에 RequestMapping에 의해 ListController가 선택되어 ModelAndView를 만든다. 
 

#### 7. ListController와 ShowController가 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.
* Servlet container에 클라이언트가 요청을 보내면 servlet container는 init()메소드에 의해 초기화된 서블릿의 service() 메소드를 실행한다. 
이때 service() 메소드는 각 요청에 대해 새로운 스레드를 만든다. 
즉, ListController와 ShowController 인스턴스는 한 개 뿐이고, 새로운 스레드를 만듦으로써 각각 요청을 처리한다. 
그렇기에, execute 메쏘드 밖에 있는 변수들은 각 요청 때마다 새로 만들어지지 않고 공유가 되어 문제가 된다. 

