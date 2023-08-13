# My Notes API

`My Notes API` is a ReST API to manage your digital sticky notes. The API provides endpoints to -
1. Register a new user
2. Authenticate a user(generate an access token)
3. Create a new note
4. List all notes
5. Retrieve a note
6. Update an existing note
7. Delete an existing note

Refer the [open API specification](./docs/openapi/my-notes.json) for more details.

# Getting Started

Following sections describes prerequisites, installation and usage of the API.

## Prerequisites

Following are the prerequisites to get the API up and running.
1. Auth0 identity provider
   1. Create an [Auth0](https://auth0.com/) account
   2. [Register](https://auth0.com/docs/get-started/auth0-overview/set-up-apis) the `My Notes API`
   3. [Create](https://auth0.com/docs/get-started/auth0-overview/create-applications) a `Regular Web Application` application
   4. Keep the `Client ID` and `Client Secret` handy for subsequent steps
2. Java JDK 17 or higher
3. PostgreSQL database

## Installation

1. Clone the repository `https://github.com/ahemanna/my-notes-api.git`
2. Set following environment variables - 
   1. DB_HOST - Postgresql host name
   2. DB_PORT - Postgresql port number
   3. DB_NAME - Name of the Postgresql database
   4. DB_USERNAME - Database username
   5. DB_PASSWORD - Database password
   6. AUTH0_CLIENT_ID - The client ID of the Auth0 application
   7. AUTH0_CLIENT_SECRET - The client secret of the Auth0 application
   8. AUTH0_MGMT_AUDIENCE - The audience of Auth0's Management API
   9. AUTH0_API_AUDIENCE - The audience of the `My Notes API` registered on Auth0
   10. AUTH0_API_HOST - The hostname of the Auth0 APIs
3. Run the maven compile command `./mvnw clean compile`

This should get the API up and running on `http://locahost:8080/`

# Contributing

Any contributions to improve this project is greatly appreciated.

If you have any suggestions, please [fork](https://docs.github.com/en/get-started/quickstart/fork-a-repo) the repository and submit a [pull request](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request).

# License

Distributed under the MIT License. See [LICENSE](./LICENSE) for more information.
