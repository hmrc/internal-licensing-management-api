
# Internal Licensing Management API

The import and export of certain goods are controlled by "licences" managed by a number of UK Government agencies (collectively, "Issuing Authorities"). 
The use of these licences is verified by HMRC on import and export declarations submitted to the Customs Declaration Service (CDS).

## Running the tests
There is a convenience script to run tests/formatting/scalafix as detailed below

```shell
./run_all_tests.sh
```
## Running locally
There is a convenience script to spin up the service and dependent stubs locally
```shell
./run_local_with_dependencies.sh
```

## Run locally and call the API locally

To test the endpoints locally, you need to pass in a valid `Authorization` header in the API calls.

These instructions were created with the help of the Confluence page
[Privileged Access to APIs - TOTP authorization and access](https://confluence.tools.tax.service.gov.uk/display/ApiPlatform/Privileged+Access+to+APIs+-+TOTP+authorization%2C+and+access).
Please refer to that page if this stops working.

1. Prerequisites:

   i.
   New [developer setup](https://confluence.tools.tax.service.gov.uk/display/DTRG/Setting+up+your+local+development+environment)
   has been done, including setting up the `WORKSPACE` variable.

   ii. Clone the following repositories inside the `WORKSPACE` directory:

    * [API Gatekeeper Frontend](https://github.com/hmrc/api-gatekeeper-frontend/)
    * [Third Party Developer Frontend](https://github.com/hmrc/third-party-developer-frontend/)
    * [TOTP Generator repository](https://github.com/hmrc/totp-generator/)

   iii. Install these programs if not already present:

    * [jq](https://jqlang.github.io/jq/) command-line JSON processor
    * [mongosh](https://www.mongodb.com/docs/mongodb-shell/install/) MongoDB command-line client

2. Use local Gatekeeper to create a privileged sandbox app.

    * Use a terminal to `./run_local_with_dependencies.sh`

    * In your browser, go to http://localhost:9684/api-gatekeeper/applications, logging in by supplying anything as PID
      and `admin-role` as the role.

    * Click the button "Add privileged or ROPC application", selecting `Sandbox` and `Privileged`.

    * IMPORTANT: Make a note of the resulting details, especially the TOTP secret which is only shown this once.

    * Manage the new application's scopes, adding `read:individuals-child-benefit, write:individuals-child-benefit`

    * Terminate the locally running Gatekeeper.

3. Use local Developer Hub to get a client secret.

    * Use a terminal to `./run_local_with_dependencies.sh`

    * In your browser, go to http://localhost:9685/developer/applications, logging in as required.

    * Navigate to the newly-created privileged app and use the menu to find the client ID.

    * Continue to create a client secret.

    * NOTE: Unlike the TOTP secret, if the client secret is forgotten, a new one can be created at any time.

    * Terminate the locally running Developer Hub.

4. Use a terminal to start this project, running `./run_local_with_dependences.sh`. This is needed to run various
   dependencies for the steps below.

5. Get the Authorization header by using a terminal in this project's root directory to run:
   ```
   ./get_auth [totp-secret] [client-id] [client-secret]
   ```
   This header lasts for 4 hours. It looks something like
   ```
   Bearer BXQ3/Tr....ue4V
   ```

6. You can then call API endpoints using curl or other tool of your choice, for example:
   ```
   curl --location --request PUT 'http://localhost:11133/REF' \
   --header 'Authorization: ??????????' \
   --header 'Content-Type: application/json' \
   --header 'Accept: application/vnd.hmrc.1.0+json' \
   --data '@testcommon/resources/ilms-request-valid.json'
   ```
   Replacing the ```????????``` with the header obtained in the previous step.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").