module Moat

  # MOAT REST API
  REST_URI = "https://sandbox.service-sync.com"
  REST_PATH = "/moat/v1"
  PACKAGE_ID = "simple-example"
  
  # Modify here with your credentials
  APPLICATION_ID = "{:your_application_id}"
  API_CLIENT_ID = "{:your_client_id}"
  API_CLIENT_SECRET = "{:your_client_secret}"
  
  # Job Service IDs
  VIBRATE_DEVICE = "urn:moat:#{APPLICATION_ID}:#{PACKAGE_ID}:VibrateDevice:1.0"
end
