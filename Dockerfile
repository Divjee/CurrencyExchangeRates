FROM openjdk:18

COPY jar/rssxmlparsing-1.0-SNAPSHOT-shaded.jar /rssxmlparsing-1.0-SNAPSHOT-shaded.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar", "/rssxmlparsing-1.0-SNAPSHOT-shaded.jar"]

CMD [""]
