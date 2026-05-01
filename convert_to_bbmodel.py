import json
import uuid

def generate_uuid():
    return str(uuid.uuid4())

def convert_json_to_bbmodel(input_path, output_path):
    with open(input_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    
    texture_map = {}
    textures = data.get('textures', {})
    texture_list = []
    
    for key, value in textures.items():
        if key == 'particle':
            continue
        if key.startswith('_'):
            continue
        texture_list.append({
            "name": value.split('/')[-1] if '/' in value else value,
            "namespace": value.split('/')[0] if '/' in value else "",
            "id": value,
            "width": 16,
            "height": 16,
            "uv_width": 16.0,
            "uv_height": 16.0,
            "particle": key == 'particle'
        })
        texture_map[key] = len(texture_list) - 1
    
    elements = data.get('elements', [])
    
    bbmodel_elements = []
    element_uuids = {}
    
    for idx, elem in enumerate(elements):
        elem_uuid = generate_uuid()
        element_uuids[idx] = elem_uuid
        
        texture_idx = 0
        faces_data = elem.get('faces', {})
        for face_name, face_data in faces_data.items():
            tex_ref = face_data.get('texture', '#0')
            if tex_ref.startswith('#'):
                tex_key = tex_ref[1:]
                if tex_key in texture_map:
                    texture_idx = texture_map[tex_key]
                    break
        
        from_pos = elem.get('from', [0, 0, 0])
        to_pos = elem.get('to', [1, 1, 1])
        
        rotation_data = elem.get('rotation', {})
        rotation_angle = rotation_data.get('angle', 0)
        rotation_axis = rotation_data.get('axis', 'y')
        rotation_origin = rotation_data.get('origin', [8, 8, 8])
        
        if rotation_angle != 0:
            rotation = [0, 0, 0]
            if rotation_axis == 'x':
                rotation[0] = rotation_angle
            elif rotation_axis == 'y':
                rotation[1] = rotation_angle
            elif rotation_axis == 'z':
                rotation[2] = rotation_angle
        else:
            rotation = [0, 0, 0]
        
        bbmodel_elem = {
            "name": elem.get('name', f'element_{idx}'),
            "uuid": elem_uuid,
            "type": "cube",
            "autouv": 0,
            "color": idx % 8,
            "origin": rotation_origin if rotation_angle != 0 else [8, 8, 8],
            "rotation": rotation,
            "from": from_pos,
            "to": to_pos,
            "faces": {}
        }
        
        for face_name in ['north', 'east', 'south', 'west', 'up', 'down']:
            if face_name in faces_data:
                face = faces_data[face_name]
                uv = face.get('uv', [0, 0, 1, 1])
                tex_ref = face.get('texture', '#0')
                face_rotation = face.get('rotation', 0)
                
                tex_idx = 0
                if tex_ref.startswith('#'):
                    tex_key = tex_ref[1:]
                    if tex_key in texture_map:
                        tex_idx = texture_map[tex_key]
                
                bbmodel_elem["faces"][face_name] = {
                    "uv": uv,
                    "texture": tex_idx,
                    "rotation": face_rotation
                }
            else:
                bbmodel_elem["faces"][face_name] = {
                    "uv": [0, 0, 1, 1],
                    "texture": 0,
                    "rotation": 0
                }
        
        bbmodel_elements.append(bbmodel_elem)
    
    CORIANDER_GROUP = "result_512x512_121"
    
    def build_outliner(groups_data):
        outliner = []
        
        for group in groups_data:
            group_uuid = generate_uuid()
            group_name = group.get('name', 'group')
            group_origin = group.get('origin', [0, 0, 0])
            children_data = group.get('children', [])
            
            children = []
            for child in children_data:
                if isinstance(child, int):
                    if child not in element_uuids:
                        continue
                    
                    if group_name == CORIANDER_GROUP:
                        children.append(element_uuids[child])
                    else:
                        bone_uuid = generate_uuid()
                        elem = elements[child]
                        from_pos = elem.get('from', [0, 0, 0])
                        to_pos = elem.get('to', [1, 1, 1])
                        center_origin = [
                            (from_pos[0] + to_pos[0]) / 2,
                            (from_pos[1] + to_pos[1]) / 2,
                            (from_pos[2] + to_pos[2]) / 2
                        ]
                        bone_group = {
                            "name": f"bone_{child}",
                            "uuid": bone_uuid,
                            "origin": center_origin,
                            "rotation": [0, 0, 0],
                            "isOpen": True,
                            "children": [element_uuids[child]]
                        }
                        children.append(bone_group)
                    
                elif isinstance(child, dict):
                    nested = build_outliner([child])
                    children.extend(nested)
            
            group_obj = {
                "name": group_name,
                "uuid": group_uuid,
                "origin": group_origin,
                "rotation": [0, 0, 0],
                "isOpen": True,
                "children": children
            }
            outliner.append(group_obj)
        
        return outliner
    
    groups_data = data.get('groups', [])
    outliner = build_outliner(groups_data)
    
    bbmodel = {
        "meta": {
            "format_version": "4.10",
            "model_format": "free",
            "box_uv": False
        },
        "name": "wok_yellow_steak_3",
        "model_identifier": "wok_yellow_steak_3",
        "visible_box": [1, -1, 2],
        "variable_placeholders": "",
        "variable_placeholder_buttons": [],
        "resolution": {
            "width": 16,
            "height": 16
        },
        "elements": bbmodel_elements,
        "outliner": outliner,
        "textures": texture_list
    }
    
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(bbmodel, f, indent=2, ensure_ascii=False)
    
    print(f"转换完成！")
    print(f"元素数量: {len(bbmodel_elements)}")
    print(f"贴图数量: {len(texture_list)}")
    
    def count_bones(items):
        count = 0
        for item in items:
            if isinstance(item, dict):
                count += 1
                count += count_bones(item.get('children', []))
        return count
    
    def print_outliner(items, indent=0):
        for item in items:
            if isinstance(item, str):
                print(f"{'  ' * indent}  元素: {item[:8]}...")
            elif isinstance(item, dict):
                elem_count = sum(1 for c in item.get('children', []) if isinstance(c, str))
                group_count = sum(1 for c in item.get('children', []) if isinstance(c, dict))
                print(f"{'  ' * indent}骨骼 '{item['name']}': {elem_count} 个元素, {group_count} 个子骨骼")
                print_outliner(item.get('children', []), indent + 1)
    
    bone_count = count_bones(outliner)
    print(f"骨骼总数: {bone_count}")
    print("\nOutliner 结构:")
    print_outliner(outliner)

if __name__ == '__main__':
    input_path = r'c:\Users\NBG\Desktop\新建文件夹 (11)\realisticdining-main\common\src\main\resources\assets\realisticdining\models\item\wok_yellow_steak_3.json'
    output_path = r'c:\Users\NBG\Desktop\新建文件夹 (11)\realisticdining-main\common\src\main\resources\assets\realisticdining\models\item\wok_yellow_steak_3.bbmodel'
    convert_json_to_bbmodel(input_path, output_path)
