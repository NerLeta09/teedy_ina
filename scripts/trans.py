# translator.py
import sys
from deep_translator import GoogleTranslator

if __name__ == "__main__":
    if len(sys.argv) < 2:
        sys.exit(1)

    text = sys.argv[1]
    translated = GoogleTranslator(source='auto', target='zh-CN').translate(text)# to Chinese
    print(translated)
