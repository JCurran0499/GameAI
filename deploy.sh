if ! [ -d packages ]; then
    pipenv run pip freeze > requirements.txt
    pip3 install -r requirements.txt -t packages/python/lib/python3.9/site-packages
fi

read -p "s3 bucket: " S3_BUCKET

sam build -t cloudformation.yaml
sam deploy -t cloudformation.yaml --stack-name GameAI --s3-bucket $S3_BUCKET
