if ! [ -d packages ]; then
    pipenv run pip freeze > requirements.txt
    pip3 install -r requirements.txt -t packages/python/lib/python3.9/site-packages
fi

if ! [ -f s3_bucket.txt ]; then
    read -p "s3 bucket: " bucket_name
    echo $bucket_name > s3_bucket.txt
fi

read -r S3_BUCKET < s3_bucket.txt

sam build -t cloudformation.yaml
sam deploy -t cloudformation.yaml --stack-name GameAI --s3-bucket $S3_BUCKET
