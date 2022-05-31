# QR Code Penalty-Calculator
Can be used to determine the best mask pattern for a QR Code.

## Features
1. Receive QR Code information like the module size (in pixels), the QR Code version, as well as the penalty score.
2. Apply all eight mask patterns on a QR Code.
3. Calculate the best mask pattern by finding the lowest penalty score.

Please note: These classes cannot be used to update information modules, too. After applying a mask pattern, you would need to change them yourself.

## How to install
Simply download both classes and move them into your project.

## How to use
Both class contructors require the QR Code as a BufferedImage. Any whitespace around the QR Code is not supported yet.
Also please make sure that all QR Code modules are perfect squares without any shadows or other visual effects.

The "Interpreter"-class defines an "on color" (foreground, normally black) and an "off color" (background, normally white). If your QR Code uses different colors, please upddate them. (They are stored in simple variables.)

## More information about…
…penalty scores: https://www.thonky.com/qr-code-tutorial/data-masking<br>
…mask patterns: https://www.thonky.com/qr-code-tutorial/mask-patterns<br>
…QR Codes: https://en.wikipedia.org/wiki/QR_Code<br>
…how to use: please check the examples folder.
