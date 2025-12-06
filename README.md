# Java-Snake-AI

A classic snake game with AI. I challenged myself to build a game structure from scratch using only standard Java and its builtin interface AWT (Abstract Window Toolkit) (no Unity or LibGDX). While it looks basic, it includes concepts like fixed-time-step game loop, double-buffering, clean MVC architecture, and a pathfinding AI that is switchable any time and plays the game better than me.

## How to Run
- Clone the repo:
`git clone https://github.com/m-enes/java-snake-ai.git`
- Compile source code files using javac:
`javac -d build src/*.java`
- Run using java:
`java -cp build Game`

## Controls
Key Action \
WASD => Move Snake \
T => Toogle AI Mode \
F => Pause / Unpause 

## TODOS
- DONE Add Vector2i
- Make logging proper (less verbose)
- (Optional) Optimize Apple spawning mechanism by generating random number in the range of free cell count then traverse to generated index (O(n^2) time complexity)
- DONE Add sprites for background, apple and snake (graphics folder credit @Clear_code, sounds folder credit @sphynx and Nokia)
- PARTIAL Add music and sound effects for eating apple, game over, and (maybe) game start and (maybe) rattle.
- DONE Add game start screen
- Add game over screen
- Add game settings (sfx and music volumes)
- (Otional) Add different game mods (speed, sprites, growthFactor, simultaneouslyAppleCount options)
- Only scale when window rescaled or optimize bufferedimage creating for double buffering
- Pause the game when window minimized (onWindowResize and width and height both equals to 0)
- DONE Add score and AI mode open/close information at the bottom bar (Fix the text also rescaling => Fixed by extra bufferedimage)
- Make score bigger when apple eaten then smoothly decrease to normal size
- Update score font size when window resized
- (Optional) Stop music loop when game over
- Make particles realistic (add velocity and lifetime)