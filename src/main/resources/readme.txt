一開始要設定資料庫，否則啟動會提示

刪除表有外鍵不好刪，這裡整理好順序了
DROP TABLE BATCH_JOB_EXECUTION_PARAMS;
DROP TABLE BATCH_STEP_EXECUTION_CONTEXT;
DROP TABLE BATCH_STEP_EXECUTION;
DROP TABLE BATCH_STEP_EXECUTION_SEQ;
DROP TABLE BATCH_JOB_EXECUTION_CONTEXT;
DROP TABLE BATCH_JOB_EXECUTION;
DROP TABLE BATCH_JOB_EXECUTION_SEQ;
DROP TABLE BATCH_JOB_INSTANCE;
DROP TABLE BATCH_JOB_SEQ;


專案轉成 web：
一、增加資料夾 src/main/webapp，裡面寫 html
二、build.gradle 增加兩個
  1. id 'war'
  2.
  implementation 'org.springframework.boot:spring-boot-starter-web'
  providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'