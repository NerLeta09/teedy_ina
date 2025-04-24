import os
import re

def remove_javadoc_from_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.readlines()
    new_content = ""
    for line in content:
        if "* @param" in line:
            continue
        if "* @return" in line:
            continue
        new_content += line
    
    return new_content

def process_directory(directory_path):
    # 遍历目录中的所有文件
    for root, dirs, files in os.walk(directory_path):
        for file_name in files:
            # 仅处理 Java 文件（或根据需要扩展其他文件类型）
            if file_name.endswith(".java"):
                file_path = os.path.join(root, file_name)
                print(f"Processing file: {file_path}")
                
                # 获取处理后的内容
                new_content = remove_javadoc_from_file(file_path)
                
                # 仅在内容有变化时才保存
                if new_content != open(file_path, 'r', encoding='utf-8').read():
                    with open(file_path, 'w', encoding='utf-8') as file:
                        file.write(new_content)
                    print(f"Updated file: {file_path}")
                else:
                    print(f"No changes for file: {file_path}")

if __name__ == "__main__":
    # 设置需要处理的目录路径
    directory_path = "./java"  # 替换为目标文件夹路径
    process_directory(directory_path)
