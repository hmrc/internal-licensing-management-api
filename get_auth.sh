#!/bin/bash

if [ $# -ne 3 ]; then
  echo Usage: get_auth.sh [totp-secret] [client-id] [client-secret]
  exit 1
fi

TOTP_SECRET=$1
CLIENT_ID=$2
CLIENT_SECRET=$3

TOTP=$(
  cd $WORKSPACE/totp-generator
  ./generate-totp.sh $TOTP_SECRET | cut -c 7-
)

TOKEN_RESPONSE=$(curl --location --request POST 'http://localhost:9613/token' \
  --header 'content-type: application/x-www-form-urlencoded' \
  --data-urlencode grant_type=client_credentials \
  --data-urlencode scope=read:individuals-child-benefit \
  --data-urlencode scope=write:individuals-child-benefit \
  --data-urlencode client_id=$CLIENT_ID \
  --data-urlencode client_secret=$TOTP$CLIENT_SECRET)

ACCESS_TOKEN=$(echo $TOKEN_RESPONSE | jq -r .access_token)

AUTH_HEADER_DATA=$(mongosh --quiet localhost/third-party-delegated-authority --eval "EJSON.stringify(
  db.delegatedAuthority.find({
    'token.accessToken' : '$ACCESS_TOKEN'
  },{
    _id: 0,
    internalAuthHeader: 1
  }).toArray())")

INTERNAL_AUTH_HEADER=$(echo $AUTH_HEADER_DATA | jq -r .[0].internalAuthHeader)

echo $INTERNAL_AUTH_HEADER