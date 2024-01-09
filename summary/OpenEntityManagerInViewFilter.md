# Open Entity Manager (또는 Session) In View Filter

## OpenEntityManagerInViewFilter 란?

- JPA의 EntityManager를 요청을 처리하는 전체 프로세스에 바인딩해주는 역할을 하는 Filter로써, <br>
  뷰가 렌더링 될때 까지 영속성 컨텍스트를 유지하기 때문에 필요한 데이터를 렌더링하는 시점에 <br>
  추가로 읽어 올 수 있도록(지연 로딩, Lazy Loading)을 함

  > **Note** <br>
  > <b>영속성 컨텍스트란?<b>
  > DataBase에서 읽어오는 객체를 관리하는 컨텍스트(JPA - EntityManager, Hibernate - Session) <br>
  > 영속성 컨텍스트 안에서 Persistence 상태의 객체들을 관리하는데, <br>
  > 트랜잭션 범위 안에서 객체 상태의 변경을 감지하다가 트랜잭션이 종료 되는 시점에 DataBase에 반영함(update query 발생)

  