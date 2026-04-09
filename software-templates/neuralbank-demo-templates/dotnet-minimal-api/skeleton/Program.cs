var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();

app.MapGet("/health", () => Results.Json(new { status = "ok" }));
app.MapGet("/v1/ping", () => Results.Json(new { message = "pong" }));

app.Run();

