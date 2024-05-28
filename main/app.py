from aws_lambda_powertools import Logger
from aws_lambda_powertools.utilities.typing import LambdaContext

from aws_lambda_powertools.event_handler import APIGatewayHttpResolver
from aws_lambda_powertools.logging.correlation_paths import API_GATEWAY_HTTP

logger = Logger()
api = APIGatewayHttpResolver()

@api.get("/tictactoe")
def tic_tac_toe():
    return api.current_event.body

@logger.inject_lambda_context(correlation_id_path=API_GATEWAY_HTTP)
def handler(event: dict, context: LambdaContext):
    return api.resolve(event, context)
