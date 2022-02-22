# Intro

Sample Spring boot app that exposes endpoints to interact with AWS S3:

* `GET /buckets` -> lists all buckets
* `GET /buckets/{bucketName}` -> gets the information for the given bucket
* `GET /buckets/{bucketName}/objects` -> gets the object summaries list for the given bucket
* `GET /buckets/{bucketName}/objects/{objectName}` -> gets the object's byte content
* `POST /buckets/{bucketName}/objects` -> uploads a multipart file as an object to the given bucket (optionally to
  directory in with the query param `subFolderPath`)

# Running it

Define the environment variables for the placeholders in `application.yml`.

