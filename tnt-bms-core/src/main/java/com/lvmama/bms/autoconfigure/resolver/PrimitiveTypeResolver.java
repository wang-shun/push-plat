package com.lvmama.bms.autoconfigure.resolver;

import com.lvmama.bms.core.commons.utils.PrimitiveTypeUtils;
import com.lvmama.bms.autoconfigure.AutoConfigContext;

import java.beans.PropertyDescriptor;

/**
 * @author Robert HG (254963746@qq.com) on 4/20/16.
 */
public class PrimitiveTypeResolver extends AbstractResolver {

    public static final PrimitiveTypeResolver INSTANCE = new PrimitiveTypeResolver();

    @Override
    public void resolve(final AutoConfigContext context, final PropertyDescriptor descriptor, final Class<?> propertyType) {

        doFilter(context, descriptor, new Filter() {
            @Override
            public boolean onCondition(String name, String key, String value) {
                return key.equals(name);
            }

            @Override
            public boolean call(String name, String key, String value) {
                Object v = PrimitiveTypeUtils.convert(value, propertyType);
                writeProperty(context, descriptor, v);
                return false;
            }
        });
    }

}
