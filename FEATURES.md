# HellasDeck

HellasDeck brings the Deck of Many Mons minigame to the Hellas Pixelmon server
suite. Players collect and spend deck tokens to pull from curated loot tables
that roll Pokémon rewards, special effects (death/protect/retry), and server-wide
broadcasts. The mod focuses purely on backend flow and staff tooling—the card
content itself lives in bundled JSON definitions that can be tweaked without
recompiling.

## Feature Highlights
- **Deck Session Lifecycle** – `/hellas deck start`, `draw`, `protect`, and `end`
  guide players through token-gated runs that mirror the tabletop experience.
- **Token Economy** – Items named `pixelmon:deck-of-many-mons` can be converted
  into tokens, spent to start a run, or converted back for trading.
- **Special Card Effects** – Built-in handling for protect, retry, and death
  cards keeps the entire ruleset server-side and cheat-resistant.
- **Reward Execution** – Each draw records the Pokémon command to run later;
  when the session ends, only protected cards survive death events and are paid
  out automatically via `/pokegive`.
- **Broadcast + Logging** – Players can broadcast their draws to the server,
  while staff get JSON logs for every run plus admin token give/take commands.

## Technical Overview
- `HellasDeck` is the Forge entry point. It validates HellasCore (via
  `CoreCheck`) and wires the command registration callback.
- The `commands` package holds Brigadier registrations for every `/hellas deck`
  subcommand. Each handler delegates to the session/token utilities and uses
  obfuscated 1.16.5 methods (e.g., `func_197030_a`) to interact with Minecraft.
- The `data` package provides runtime state and IO:
  - `DeckOfManyMons`, `DeckSessions`, and `TokenManager` orchestrate sessions.
  - `CardTypeLoader` and `TableLoader` load the bundled JSON tables from
    `resources/data/` and cache them in memory.
  - `Logger` writes persistent audit trails under `config/hellas/deck/logs`.
- `RewardHelper` turns the recorded draws into `/pokegive` commands executed on
  the server thread.

## Extending the Mod
- **Adding card categories** – Update `resources/data/card_types.json` with a new
  entry (see `CardTypeData`) and add a matching JSON file under
  `resources/data/tables/<type>.json` mapping display names to `pokegive`
  payloads.
- **Customising odds** – Adjust the `amount` values in `card_types.json` to
  rebalance how often a category appears during `DeckOfManyMons#drawCard()`.
- **Staff tooling** – Reuse `DeckSessions` and `TokenManager` in new commands to
  add moderation flows; they are thread-safe and built for reuse.
- **Reward hooks** – Wrap `RewardHelper.giveCards` or use the recorded command
  data from `DeckOfManyMons#getCommandData()` for bespoke payouts.

## Dependencies & Environment
- Minecraft 1.16.5 mappings with Forge `1.16.5-36.2.42` (see `build.gradle`).
- HellasControl core library (validated by `CoreCheck`).
- Pixelmon mod providing the `deck-of-many-mons` item and `/pokegive` command.
- Designed for dedicated servers; entitlement checks run when `FMLEnvironment`
  reports `Dist.DEDICATED_SERVER`.

## Notes for Future Migration
- Command logic relies on obfuscated 1.16.5 method names. Updating to newer
  Minecraft/Forge versions will require remapping the numerous
  `CommandSource.func_*` and `ServerPlayerEntity.func_*` calls.
- Reward delivery is tightly coupled to Pixelmon's `/pokegive` command format
  and the `pixelmon:deck-of-many-mons` item id. Any upstream rename will require
  adjustments in `RewardHelper`, `DeckInputCommand`, and `DeckOutputCommand`.
- Deck session state is currently in-memory only. Migrating to persistent
  storage or asynchronous execution should keep `DeckSessions` as the API
  surface to minimise churn across commands.
