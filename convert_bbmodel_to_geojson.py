import json
import re

def convert_bbmodel_to_geojson(bbmodel_path, output_path):
    with open(bbmodel_path, 'r', encoding='utf-8') as f:
        bbmodel = json.load(f)
    
    model_name = bbmodel.get('name', 'model')
    model_identifier = bbmodel.get('model_identifier', model_name)
    
    elements = bbmodel.get('elements', [])
    outliner = bbmodel.get('outliner', [])
    
    def build_bones(outliner_items, elements_map):
        bones = []
        
        for item in outliner_items:
            if isinstance(item, dict):
                bone_name = item.get('name', 'bone')
                bone_uuid = item.get('uuid', '')
                origin = item.get('origin', [0, 0, 0])
                children = item.get('children', [])
                
                bone_cubes = []
                for child in children:
                    if isinstance(child, str) and child in elements_map:
                        elem = elements_map[child]
                        cube = convert_element_to_cube(elem)
                        bone_cubes.append(cube)
                
                bone = {
                    "name": bone_name,
                    "pivot": origin,
                    "cubes": bone_cubes
                }
                bones.append(bone)
                
                nested_bones = build_bones(children, elements_map)
                bones.extend(nested_bones)
        
        return bones
    
    elements_map = {}
    for elem in elements:
        elem_uuid = elem.get('uuid', '')
        if elem_uuid:
            elements_map[elem_uuid] = elem
    
    bones = build_bones(outliner, elements_map)
    
    geojson = {
        "format_version": "1.12.0",
        "minecraft:geometry": [
            {
                "description": {
                    "identifier": f"geometry.{model_identifier}",
                    "texture_width": 16,
                    "texture_height": 16,
                    "visible_bounds_width": 3,
                    "visible_bounds_height": 2.5,
                    "visible_bounds_offset": [0, 0.75, 0]
                },
                "bones": bones
            }
        ]
    }
    
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(geojson, f, indent=2, ensure_ascii=False)
    
    print(f"转换完成：{bbmodel_path} -> {output_path}")
    print(f"  骨骼数量：{len(bones)}")

def convert_element_to_cube(elem):
    from_pos = elem.get('from', [0, 0, 0])
    to_pos = elem.get('to', [1, 1, 1])
    faces = elem.get('faces', {})
    
    size = [
        round(to_pos[0] - from_pos[0], 6),
        round(to_pos[1] - from_pos[1], 6),
        round(to_pos[2] - from_pos[2], 6)
    ]
    
    uv = {}
    for face_name in ['north', 'east', 'south', 'west', 'up', 'down']:
        if face_name in faces:
            face = faces[face_name]
            face_uv = face.get('uv', [0, 0, 1, 1])
            uv[face_name] = [
                round(min(face_uv[0], face_uv[2]), 4),
                round(min(face_uv[1], face_uv[3]), 4),
                round(max(face_uv[0], face_uv[2]), 4),
                round(max(face_uv[1], face_uv[3]), 4)
            ]
    
    cube = {
        "origin": [round(from_pos[0], 6), round(from_pos[1], 6), round(from_pos[2], 6)],
        "size": size,
        "uv": uv
    }
    
    return cube

if __name__ == '__main__':
    base_path = r'c:\Users\NBG\Desktop\新建文件夹 (11)\realisticdining-main\common\src\main\resources\assets\realisticdining\models\item'
    output_base = r'c:\Users\NBG\Desktop\新建文件夹 (11)\realisticdining-main\common\src\main\resources\assets\realisticdining\geo'
    
    files_to_convert = [
        ('wok_yellow_steak_3.bbmodel', 'wok_yellow_steak_3.geo.json'),
        ('wok_yellow_steak_3_1.bbmodel', 'wok_yellow_steak_3_1.geo.json'),
        ('wok_yellow_steak_3_2.bbmodel', 'wok_yellow_steak_3_2.geo.json'),
        ('wok_yellow_steak_3_3.bbmodel', 'wok_yellow_steak_3_3.geo.json'),
    ]
    
    for bbmodel_file, geo_file in files_to_convert:
        bbmodel_path = f"{base_path}\\{bbmodel_file}"
        output_path = f"{output_base}\\{geo_file}"
        convert_bbmodel_to_geojson(bbmodel_path, output_path)
    
    print("\n所有文件转换完成！")
