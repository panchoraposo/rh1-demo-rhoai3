from fastapi import FastAPI

app = FastAPI(title="Neuralbank AI Assistant API")


@app.get("/health")
def health():
    return {"status": "ok"}


@app.post("/v1/assist")
def assist(request: dict):
    prompt = (request or {}).get("prompt", "")
    return {
        "response": f"Received: {prompt}",
        "confidence": 0.83,
        "model": "neuralbank-assistant",
    }
