from aws_lambda_powertools import Logger
from aws_lambda_powertools.utilities.typing import LambdaContext

from aws_lambda_powertools.event_handler import APIGatewayHttpResolver
from aws_lambda_powertools.logging.correlation_paths import API_GATEWAY_HTTP

logger = Logger()
api = APIGatewayHttpResolver()

@api.get("/health")
def get_health():
    return {
        "status": "OK"
    }

@logger.inject_lambda_context(correlation_id_path=API_GATEWAY_HTTP)
def handler(event: dict, context: LambdaContext):
    logger.info("Invoking...")
    return api.resolve(event, context)
