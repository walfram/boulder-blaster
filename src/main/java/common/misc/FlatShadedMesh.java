package common.misc;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Mesh.Mode;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.util.BufferUtils;

public final class FlatShadedMesh {

	private final Mesh source;

	public FlatShadedMesh(Mesh source) {
		this.source = source;
	}

	public Mesh create() {
		FloatBuffer positionBuffer = source.getFloatBuffer(Type.Position);
		List<Vector3f> verts = Arrays.asList(BufferUtils.getVector3Array(positionBuffer));

		IndexBuffer indexBuffer = source.getIndexBuffer();

		List<Integer> inds = new ArrayList<>(indexBuffer.size());
		for (int i = 0; i < indexBuffer.size(); i++) {
			inds.add(indexBuffer.get(i));
		}

		List<Vector3f> vertices = new ArrayList<>(source.getTriangleCount() * 3);
		List<Integer> indices = new ArrayList<>(source.getTriangleCount() * 3);
		List<Vector3f> normals = new ArrayList<>(source.getTriangleCount() * 3);

		int idx = 0;
		for (List<Integer> t : Lists.partition(inds, 3)) {
			Vector3f v0 = verts.get(t.get(0));
			Vector3f v1 = verts.get(t.get(1));
			Vector3f v2 = verts.get(t.get(2));

			Triangle tr = new Triangle(v0, v1, v2);
			Vector3f normal = tr.getNormal();

			vertices.add(v0);
			vertices.add(v1);
			vertices.add(v2);

			indices.add(idx++);
			indices.add(idx++);
			indices.add(idx++);

			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
		}

		Mesh mesh = new Mesh();
		mesh.setMode(Mode.Triangles);

		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(Iterables.toArray(vertices, Vector3f.class)));

		mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(Ints.toArray(indices)));

		mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(Iterables.toArray(normals, Vector3f.class)));

		mesh.updateCounts();
		mesh.updateBound();
		
		return mesh;
	}

}
